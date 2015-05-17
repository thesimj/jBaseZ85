# Java implementation of ZeroMQ Base-85 encoding

ZeroMQ Base85 encoder/decoder written in Java.

The basic need for a binary-to-text encoding comes from a need to communicate arbitrary binary data over pre-existing 
communications protocols that were designed to carry only human-readable text. Those communication protocols may only 
be 7-bit safe (and within that avoid certain ASCII control codes), and may require line breaks at certain maximum 
intervals, and may not maintain white space. Thus, only the 95 printable ASCII characters are "safe" to use to convey data.

The ZeroMQ project uses a slightly modified representation of base85 in ASCII so it can be better embedded 
in XML Strings. It is called Z85 and use the characters 
<code>0…9, a…z, A…Z,., -, :, +, =, ^, !, /, *, ?, &, <, >, (, ), [, ], {, }, @, %, $, #.</code>