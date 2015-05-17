/*
* Copyright (c) 2015, Bubelich Mykola (bubelich.com)
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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Author: Bubelich Mykola
 * Date: 2015-05-17
 *
 * JUnit 4 Tests
 * Implementation of Base64 data encoding/decoding
 *
 * @author Bubelich Mykola (bubelich.com)
 * @link https://github.com/thesimj/jBaseZ85 (github)
 */
public class BaseZ85Test extends Assert{

    @Test
    public void test_encode() throws Exception {

        // Without padding, normal hello world :) //
        byte [] in_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B};
        assertEquals("Assert first HelloWorld", "HelloWorld", BaseZ85.encode(in_test));

        // With 1-th padding //
        in_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B, (byte) 0xAF};
        assertEquals("With 1 byte padding", "HelloWorld52", BaseZ85.encode(in_test));

        // With 2-th padding //
        in_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B, (byte) 0xAF, (byte) 0xBF};
        assertEquals("With 2 byte padding", "HelloWorldqj6", BaseZ85.encode(in_test));

        // With 3-th padding //
        in_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B, (byte) 0xAF, (byte) 0xBF, (byte) 0xCF};
        assertEquals("With 3 byte padding","HelloWorld-e:i",BaseZ85.encode(in_test));

        // Test all byte variation //
        in_test = new byte[256];
        for (int i = 0; i < in_test.length; i++) {
            in_test[i] = (byte) i;
        }

        String tststr = "009c61o!#m2NH?C3>iWS5d]J*6CRx17-skh9337xar." +
                        "{NbQB=+c[cR@eg&FcfFLssg=mfIi5%2YjuU>)kTv.7l" +
                        "}6Nnnj=ADoIFnTp/ga?r8($2sxO*itWpVyu$0IOwmYv" +
                        "=xLzi%y&a6dAb/]tBAI+JCZjQZE0{D[FpSr8GOteoH(" +
                        "41EJe-<UKDCY&L:dM3N3<zjOsMmzPRn9PQ[%@^ShV!$" +
                        "TGwUeU^7HuW6^uKXvGh.YUh4]Z})[9-kP:p:JqPF+*1" +
                        "CV^9Zp<!yAd4/Xb0k*$*&A&nJXQ<MkK!>&}x#)cTlf[" +
                        "Bu8v].4}L}1:^-@qDS{";

        assertEquals("Full byte field test", tststr, BaseZ85.encode(in_test));

    }

    @Test
    public void test_decode() throws Exception {

        // Without padding, normal hello world :) //
        byte [] out_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B};
        assertArrayEquals("Assert first HelloWorld", out_test, BaseZ85.decode("HelloWorld"));

        // With 1-th padding //
        out_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B, (byte) 0xAF};
        assertArrayEquals("With 1 byte padding", out_test, BaseZ85.decode("HelloWorld52"));

        // With 2-th padding //
        out_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B, (byte) 0xAF, (byte) 0xBF};
        assertArrayEquals("With 2 byte padding", out_test, BaseZ85.decode("HelloWorldqj6"));

        // With 3-th padding //
        out_test = new byte[]{(byte) 0x86, (byte) 0x4F, (byte) 0xD2, (byte) 0x6F, (byte) 0xB5, (byte) 0x59, (byte) 0xF7, (byte) 0x5B, (byte) 0xAF, (byte) 0xBF, (byte) 0xCF};
        assertArrayEquals("With 3 byte padding", out_test, BaseZ85.decode("HelloWorld-e:i"));

    }

    @Test
    public void test_full_cycle() throws Exception {

        // Generate random data //
        byte [] seed_test = SecureRandom.getSeed(1024);

        // Encode 1024 byte (no padding) //
        assertArrayEquals("Test 1024 random byte encode -> decode",seed_test,BaseZ85.decode(BaseZ85.encode(seed_test)));

        // Generate random data //
        seed_test = SecureRandom.getSeed(1025);

        // Encode 1025 byte (1 byte padding) //
        assertArrayEquals("Test 1025 (1 byte padding) random byte encode -> decode",seed_test,BaseZ85.decode(BaseZ85.encode(seed_test)));

        // Generate random data //
        seed_test = SecureRandom.getSeed(1026);

        // Encode 1026 byte (2 byte padding) //
        assertArrayEquals("Test 1026 (2 byte padding) random byte encode -> decode",seed_test,BaseZ85.decode(BaseZ85.encode(seed_test)));

        // Generate random data //
        seed_test = SecureRandom.getSeed(1027);

        // Encode 1027 byte (3 byte padding) //
        assertArrayEquals("Test 1027 (3 byte padding) random byte encode -> decode", seed_test, BaseZ85.decode(BaseZ85.encode(seed_test)));
    }

    @Ignore
    @Test
    public void test_size() throws Exception{

        // Kryptos text //
        String seed_string ="EMUFPHZLRFAXYUSDJKZLDKRNSHGNFIVJ\n" +
                            "YQTQUXQBQVYUVLLTREVJYQTMKYRDMFD\n" +
                            "VFPJUDEEHZWETZYVGWHKKQETGFQJNCE\n" +
                            "GGWHKK?DQMCPFQZDQMMIAGPFXHQRLG\n" +
                            "TIMVMZJANQLVKQEDAGDVFRPJUNGEUNA\n" +
                            "QZGZLECGYUXUEENJTBJLBQCRTBJDFHRR\n" +
                            "YIZETKZEMVDUFKSJHKFWHKUWQLSZFTI\n" +
                            "HHDDDUVH?DWKBFUFPWNTDFIYCUQZERE\n" +
                            "EVLDKFEZMOQQJLTTUGSYQPFEUNLAVIDX\n" +
                            "FLGGTEZ?FKZBSFDQVGOGIPUFXHHDRKF\n" +
                            "FHQNTGPUAECNUVPDJMQCLQUMUNEDFQ\n" +
                            "ELZZVRRGKFFVOEEXBDMVPNFQXEZLGRE\n" +
                            "DNQFMPNZGLFLPMRJQYALMGNUVPDXVKP\n" +
                            "DQUMEBEDMHDAFMJGZNUPLGEWJLLAETGE\n" +
                            "NDYAHROHNLSRHEOCPTEOIBIDYSHNAIA\n" +
                            "CHTNREYULDSLLSLLNOHSNOSMRWXMNE\n" +
                            "TPRNGATIHNRARPESLNNELEBLPIIACAE\n" +
                            "WMTWNDITEENRAHCTENEUDRETNHAEOE\n" +
                            "TFOLSEDTIWENHAEIOYTEYQHEENCTAYCR\n" +
                            "EIFTBRSPAMHHEWENATAMATEGYEERLB\n" +
                            "TEEFOASFIOTUETUAEOTOARMAEERTNRTI\n" +
                            "BSEDDNIAAHTTMSTEWPIEROAGRIEWFEB\n" +
                            "AECTDDHILCEIHSITEGOEAOSDDRYDLORIT\n" +
                            "RKLMLEHAGTDHARDPNEOHMGFMFEUHE\n" +
                            "ECDMRIPFEIMEHNLSSTTRTVDOHW?OBKR\n" +
                            "UOXOGHULBSOLIFBBWFLRVQQPRNGKSSO\n" +
                            "TWTQSJQSSEKZZWATJKLUDIAWINFBNYP\n" +
                            "VTTMZFPKWGDKZXTJCDIGKUHUAUEKCAR";

//        byte [] seed_test = seed_string.concat(seed_string.concat(seed_string)).getBytes("UTF-8");
        byte [] seed_test = SecureRandom.getSeed(1024 * 1024);

        // Encode by Base64 //
        String b64 = Base64.getEncoder().encodeToString(seed_test);

        // Encode to BaseZ85 //
        String bz85 = BaseZ85.encode(seed_test);

        // Show results //
        Logger.getAnonymousLogger().info(String.format("Base64 length: %d, BaseZ85 length: %d", b64.length(), bz85.length()));
    }
}