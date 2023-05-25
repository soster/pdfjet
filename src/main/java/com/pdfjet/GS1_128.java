/**
 *  GS1_128.java
 *
Copyright 2023 Innovatics Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.pdfjet;

class GS1_128 {
public static final int[] TABLE = {
212222, // 0
222122, // 1
222221, // 2
121223, // 3
121322, // 4
131222, // 5
122213, // 6
122312, // 7
132212, // 8
221213, // 9
221312, // 10
231212, // 11
112232, // 12
122132, // 13
122231, // 14
113222, // 15
123122, // 16
123221, // 17
223211, // 18
221132, // 19
221231, // 20
213212, // 21
223112, // 22
312131, // 23
311222, // 24
321122, // 25
321221, // 26
312212, // 27
322112, // 28
322211, // 29
212123, // 30
212321, // 31
232121, // 32
111323, // 33
131123, // 34
131321, // 35
112313, // 36
132113, // 37
132311, // 38
211313, // 39
231113, // 40
231311, // 41
112133, // 42
112331, // 43
132131, // 44
113123, // 45
113321, // 46
133121, // 47
313121, // 48
211331, // 49
231131, // 50
213113, // 51
213311, // 52
213131, // 53
311123, // 54
311321, // 55
331121, // 56
312113, // 57
312311, // 58
332111, // 59
314111, // 60
221411, // 61
431111, // 62
111224, // 63
111422, // 64
121124, // 65
121421, // 66
141122, // 67
141221, // 68
112214, // 69
112412, // 70
122114, // 71
122411, // 72
142112, // 73
142211, // 74
241211, // 75
221114, // 76
413111, // 77
241112, // 78
134111, // 79
111242, // 80
121142, // 81
121241, // 82
114212, // 83
124112, // 84
124211, // 85
411212, // 86
421112, // 87
421211, // 88
212141, // 89
214121, // 90
412121, // 91
111143, // 92
111341, // 93
131141, // 94
114113, // 95
114311, // 96
411113, // 97
411311, // 98  - SHIFT
113141, // 99  - Code C
114131, // 100 - FNC 4
311141, // 101 - Code A
411131, // 102
211412, // 103 - Start A
211214, // 104 - Start B
211232, // 105 - Start C
2331112,// 106 - Stop
};

// Shifts from SET_A -> SET_B or SET_B -> SET_A for a single char
public static final int SHIFT = 98;

public static final int CODE_C = 99;    // Latch to SET_C
public static final int FNC_4 = 100;    // FNC 4
public static final int CODE_A = 101;   // Latch to SET_A

public static final int START_A = 103;
public static final int START_B = 104;
public static final int START_C = 105;
public static final int STOP = 106;
}   // End of GS1_128.java
