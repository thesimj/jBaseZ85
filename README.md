# Java implementation of ZeroMQ Base-85 encoding
[![Build Status](https://travis-ci.org/thesimj/jBaseZ85.png?branch=master)](https://travis-ci.org/thesimj/jBaseZ85)

ZeroMQ Base85 encoder/decoder written in Java.

The basic need for a binary-to-text encoding comes from a need to communicate arbitrary binary data over pre-existing 
communications protocols that were designed to carry only human-readable text. Those communication protocols may only 
be 7-bit safe (and within that avoid certain ASCII control codes), and may require line breaks at certain maximum 
intervals, and may not maintain white space. Thus, only the 95 printable ASCII characters are "safe" to use to convey data.

The ZeroMQ project uses a slightly modified representation of base85 in ASCII so it can be better embedded 
in XML Strings. It is called Z85 and use the characters 
``0-9, a-z, A-Z,., -, :, +, =, ^, !, /, *, ?, &, <, >, (, ), [, ], {, }, @, %, $, #.``

## Installation

```
Import src file /src/main/java/com/bubelich/jBaseZ85.java for main class.
Import src file /src/test/java/com/bubelich/jBaseZ85Test.java for JUnit 4.0 tests.
```

## Usage

### Encoding:

```java
import com.bubelich;
...
byte [] data = "Hello world!".getBytes();
String encdata = jBaseZ85.encode(data);
System.out.println("jBaseZ85 encoded data: " + encdata); // jBaseZ85 encoded data: nm=QNzY&b1^)hc
```

### Decoding:

```java
import com.bubelich;
...
String encdata = "nm=QNzY&b1^)hc";
byte [] data = jBaseZ85.decode(encdata);
System.out.println("jBaseZ85 decoded data: " + new String(data)); // jBaseZ85 decoded data: Hello World
```

## Which specification to use?

ZeroMQ appears to be a better specification for mostK applications. It doesn't
include quotes in its alphabet which makes it useful in many quoted languages
(such as C, C++, JavaScript, Java, Python, Perl, Ruby... the list goes on).
Neither does it add the 4 extra enclosing bytes Ascii85 does.  There may,
however, be some problems using it in SGML and its derivatives since
both less-than `<` and greater-than `>` are part of the alphabet. But
then again, Ascii85 has that as well.

* [Base64]: http://en.wikipedia.org/wiki/Base64
* [Base85]: http://en.wikipedia.org/wiki/Ascii85
* [NodeBuffer]: http://nodejs.org/api/buffer.html
* [NodeBufferToString]: http://nodejs.org/api/buffer.html#buffer_buf_tostring_encoding_start_end
* [Base85ZeroMQ]: http://rfc.zeromq.org/spec:32
* [Base85IPv6]: http://tools.ietf.org/html/rfc1924
* [JSCompare]: http://stackoverflow.com/questions/359494/does-it-matter-which-equals-operator-vs-i-use-in-javascript-comparisons
* [SGML]: https://en.wikipedia.org/wiki/Standard_Generalized_Markup_Language
* [JavaScriptIPv6]: https://github.com/beaugunderson/javascript-ipv6
* [nodejs]: https://github.com/noseglid/base85