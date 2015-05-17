/*
* Copyright (c) 2015, Bubelich Mykola
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
*
* Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* Neither the name of the copyright holder nor the names of its contributors
* may be used to endorse or promote products derived from this software without
* specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/

package com.bubelich.basez85;

import com.sun.istack.internal.Nullable;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BaseZ85 {
    private final static char [] alphabets = {
            '0','1','2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z','A','B','C','D',
            'E','F','G','H','I','J','K','L','M','N',
            'O','P','Q','R','S','T','U','V','W','X',
            'Y','Z','.','-',':','+','=','^','!','/',
            '*','?','&','<','>','(',')','[',']','{',
            '}','@','%','$','#'};

    private final static int [] rev_alphabets = {
            68,0,84,83,82,72,0,75,76,70,65,0,63,62,
            69,0,1,2,3,4,5,6,7,8,9,64,0,73,66,74,71,
            81,36,37,38,39,40,41,42,43,44,45,46,47,
            48,49,50,51,52,53,54,55,56,57,58,59,60,61,
            77,0,78,67,0,0,10,11,12,13,14,15,16,17,18,
            19,20,21,22,23,24,25,26,27,28,29,30,31,32,
            33,34,35,79,0,80};

    private final static int    rev_alphabets_shift = 33;


//    final static long [] tms = new long[]{0x31C84B1,0x95EED,0x1C39,0x55,0x01};

    @Nullable
    private static char[] encodeQuarter(byte[] data){

        long value = (data[0] & 0x00000000000000FFL) |
                    ((data[1] & 0x00000000000000FFL) <<  8) |
                    ((data[2] & 0x00000000000000FFL) << 16) |
                    ((data[3] & 0x00000000000000FFL) << 24);

        char [] out = new char[5];

        out[0] = alphabets[ (int)((value / 0x31C84B1L) % 85) ];
        out[1] = alphabets[ (int)((value / 0x95EEDL) % 85) ];
        out[2] = alphabets[ (int)((value / 0x1C39L) % 85) ];
        out[3] = alphabets[ (int)((value / 0x55L) % 85) ];
        out[4] = alphabets[ (int)((value) % 85) ];

        return out;
    }

    @Nullable
    private static char[] encodePadding(byte [] data){

        long value = 0;
        int length = (data.length * 5/4) + 1;
        char [] out = new char[length];

        switch (data.length){
            case 3 : value |= (data[2] & 0x00000000000000FFL) << 16;
            case 2 : value |= (data[1] & 0x00000000000000FFL) << 8;
        }

        value |= (data[0] & 0x00000000000000FFL);

        //
        switch (data.length){
            case 3 :
                out[3] = alphabets[ (int)((value / 0x95EEDL) % 85) ];
            case 2:
                out[2] = alphabets[ (int)((value / 0x1C39L) % 85) ];
        }

        out[1] = alphabets[ (int)((value / 0x55L) % 85) ];
        out[0] = alphabets[ (int)((value) % 85) ];



        return out;
    }

    @Nullable
    public static String encode(byte [] data){

        int length = data.length;
        int index = 0;
        byte [] buff = new byte[4];

        StringBuilder sb = new StringBuilder( (data.length * 5/4) + 1);

        while (length >= 4 ) {

            // copy data to buff //
            buff[3] = data[index++];
            buff[2] = data[index++];
            buff[1] = data[index++];
            buff[0] = data[index++];

            sb.append(encodeQuarter(buff));

            length -= 4;
        }

        // Padding zone //
        if(length > 0) {

            buff = new byte[length];

            for (int i = length-1; i >= 0; i--) {
                buff[i] = data[index++];
            }

            sb.append(encodePadding(buff));
        }

        return sb.toString();
    }

//    -- DECODE SECTION -- //

    @Nullable
    public static byte[] decode(String data){
        return decode(data.toCharArray());
    }

    @Nullable
    public static byte[] decode(char [] data){

        int length = data.length;
        int index = 0;

        char[] buff = new char[5];

        ByteBuffer bytebuff = ByteBuffer.allocate( (length * 4/5) );

        while (length >= 5){

            buff[0] = data[index++];
            buff[1] = data[index++];
            buff[2] = data[index++];
            buff[3] = data[index++];
            buff[4] = data[index++];

            bytebuff.put(decodeQuarter(buff));

            length -= 5;
        }

        // If length > 0 Then need padding //
        if(length > 0) {

            // create padding buffer //
            char [] padding = new char[length];

            // copy last data value to padding buffer //
            for (int i = 0; i < length; i++)
                padding[i] = data[index++];

            // decode padding //
            bytebuff.put(decodePadding(padding));
        }

        bytebuff.flip();

        return bytebuff.limit() > 0 ?
                Arrays.copyOf(bytebuff.array(), bytebuff.limit())
                : null;
    }

    private static byte [] decodeQuarter(char [] data){

        long value = 0;

        value += rev_alphabets[data[0] - rev_alphabets_shift] * 0x31C84B1L;
        value += rev_alphabets[data[1] - rev_alphabets_shift] * 0x95EEDL;
        value += rev_alphabets[data[2] - rev_alphabets_shift] * 0x1C39L;
        value += rev_alphabets[data[3] - rev_alphabets_shift] * 0x55L;
        value += rev_alphabets[data[4] - rev_alphabets_shift];

        return new byte[] { (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) (value) };

    }

    private static byte [] decodePadding(char [] data){

        long value = 0;
        int length = data.length * 4 / 5;

        switch (data.length){
            case 4 : value += rev_alphabets[data[3] - rev_alphabets_shift] * 0x95EEDL;
            case 3 : value += rev_alphabets[data[2] - rev_alphabets_shift] * 0x1C39L;
            case 2 : value += rev_alphabets[data[1] - rev_alphabets_shift] * 0x55L;
        }

        value += rev_alphabets[data[0] - rev_alphabets_shift];

        byte [] buff = new byte[length];


        for (int i = length - 1; i >= 0; i--) {
            buff[length-i-1] = (byte)(value >>> 8 * i);
        }

        return buff;
    }
}
