/**
 *  TextCompact.java
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

class TextCompact {
/*
 * The following table contains:
 * ASCII Value, Base 30 Value, Code specifying in what subtable is the character.
 */
public static final int[][] TABLE = {
{0,26,0x04},    // space
{1,26,0x04},    // space
{2,26,0x04},    // space
{3,26,0x04},    // space
{4,26,0x04},    // space
{5,26,0x04},    // space
{6,26,0x04},    // space
{7,26,0x04},    // space
{8,26,0x04},    // space
{9,12,0x02},    // HT
{10,15,0x01},    // LF
{11,26,0x04},    // space
{12,26,0x04},    // space
{13,11,0x02},    // CR
{14,26,0x04},    // space
{15,26,0x04},    // space
{16,26,0x04},    // space
{17,26,0x04},    // space
{18,26,0x04},    // space
{19,26,0x04},    // space
{20,26,0x04},    // space
{21,26,0x04},    // space
{22,26,0x04},    // space
{23,26,0x04},    // space
{24,26,0x04},    // space
{25,26,0x04},    // space
{26,26,0x04},    // space
{27,26,0x04},    // space
{28,26,0x04},    // space
{29,26,0x04},    // space
{30,26,0x04},    // space
{31,26,0x04},    // space
{32,26,0x04},    // space
{33,10,0x01},    // !
{34,20,0x01},    // "
{35,15,0x02},    // #
{36,18,0x02},    // $
{37,21,0x02},    // %
{38,10,0x02},    // &
{39,28,0x01},    // '
{40,23,0x01},    // (
{41,24,0x01},    // )
{42,22,0x02},    // *
{43,20,0x02},    // +
{44,13,0x02},    // ,
{45,16,0x02},    // -
{46,17,0x02},    // .
{47,19,0x02},    // /
{48, 0,0x02},    // 0
{49, 1,0x02},    // 1
{50, 2,0x02},    // 2
{51, 3,0x02},    // 3
{52, 4,0x02},    // 4
{53, 5,0x02},    // 5
{54, 6,0x02},    // 6
{55, 7,0x02},    // 7
{56, 8,0x02},    // 8
{57, 9,0x02},    // 9
{58,14,0x02},    // :
{59, 0,0x01},    // ;
{60, 1,0x01},    // <
{61,23,0x02},    // =
{62, 2,0x01},    // >
{63,25,0x01},    // ?
{64, 3,0x01},    // @
{65, 0,0x08},    // A
{66, 1,0x08},    // B
{67, 2,0x08},    // C
{68, 3,0x08},    // D
{69, 4,0x08},    // E
{70, 5,0x08},    // F
{71, 6,0x08},    // G
{72, 7,0x08},    // H
{73, 8,0x08},    // I
{74, 9,0x08},    // J
{75,10,0x08},    // K
{76,11,0x08},    // L
{77,12,0x08},    // M
{78,13,0x08},    // N
{79,14,0x08},    // O
{80,15,0x08},    // P
{81,16,0x08},    // Q
{82,17,0x08},    // R
{83,18,0x08},    // S
{84,19,0x08},    // T
{85,20,0x08},    // U
{86,21,0x08},    // V
{87,22,0x08},    // W
{88,23,0x08},    // X
{89,24,0x08},    // Y
{90,25,0x08},    // Z
{91, 4,0x01},    // [
{92, 5,0x01},    // \
{93, 6,0x01},    // ]
{94,24,0x02},    // ^
{95, 7,0x01},    // _
{96, 8,0x01},    // `
{97, 0,0x04},    // a
{98, 1,0x04},    // b
{99, 2,0x04},    // c
{100,3,0x04},    // d
{101,4,0x04},    // e
{102,5,0x04},    // f
{103,6,0x04},    // g
{104,7,0x04},    // h
{105,8,0x04},    // i
{106,9,0x04},    // j
{107,10,0x04},    // k
{108,11,0x04},    // l
{109,12,0x04},    // m
{110,13,0x04},    // n
{111,14,0x04},    // o
{112,15,0x04},    // p
{113,16,0x04},    // q
{114,17,0x04},    // r
{115,18,0x04},    // s
{116,19,0x04},    // t
{117,20,0x04},    // u
{118,21,0x04},    // v
{119,22,0x04},    // w
{120,23,0x04},    // x
{121,24,0x04},    // y
{122,25,0x04},    // z
{123,26,0x01},    // {
{124,21,0x01},    // |
{125,27,0x01},    // }
{126, 9,0x01},    // ~
};
}   // End of TextCompact.java
