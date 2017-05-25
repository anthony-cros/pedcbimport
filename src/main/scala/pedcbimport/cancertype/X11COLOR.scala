package pedcbimport.cancertype

import enumeratum.EnumEntry
import enumeratum.Enum
import enumeratum.PlayJsonEnum
  
// ===========================================================================
sealed trait X11COLOR // https://www.w3.org/TR/css3-color/#svg-color
    extends EnumEntry
  object X11COLOR
      extends Enum[X11COLOR]
         with PlayJsonEnum[X11COLOR] {
    val values = findValues

    case object `aliceblue` extends X11COLOR // #F0F8FF; 240,248,255
    case object `antiquewhite` extends X11COLOR // #FAEBD7; 250,235,215
    case object `aqua` extends X11COLOR // #00FFFF; 0,255,255
    case object `aquamarine` extends X11COLOR // #7FFFD4; 127,255,212
    case object `azure` extends X11COLOR // #F0FFFF; 240,255,255
    case object `beige` extends X11COLOR // #F5F5DC; 245,245,220
    case object `bisque` extends X11COLOR // #FFE4C4; 255,228,196
    case object `black` extends X11COLOR // #000000; 0,0,0
    case object `blanchedalmond` extends X11COLOR // #FFEBCD; 255,235,205
    case object `blue` extends X11COLOR // #0000FF; 0,0,255
    case object `blueviolet` extends X11COLOR // #8A2BE2; 138,43,226
    case object `brown` extends X11COLOR // #A52A2A; 165,42,42
    case object `burlywood` extends X11COLOR // #DEB887; 222,184,135
    case object `cadetblue` extends X11COLOR // #5F9EA0; 95,158,160
    case object `chartreuse` extends X11COLOR // #7FFF00; 127,255,0
    case object `chocolate` extends X11COLOR // #D2691E; 210,105,30
    case object `coral` extends X11COLOR // #FF7F50; 255,127,80
    case object `cornflowerblue` extends X11COLOR // #6495ED; 100,149,237
    case object `cornsilk` extends X11COLOR // #FFF8DC; 255,248,220
    case object `crimson` extends X11COLOR // #DC143C; 220,20,60
    case object `cyan` extends X11COLOR // #00FFFF; 0,255,255
    case object `darkblue` extends X11COLOR // #00008B; 0,0,139
    case object `darkcyan` extends X11COLOR // #008B8B; 0,139,139
    case object `darkgoldenrod` extends X11COLOR // #B8860B; 184,134,11
    case object `darkgray` extends X11COLOR // #A9A9A9; 169,169,169
    case object `darkgreen` extends X11COLOR // #006400; 0,100,0
    case object `darkgrey` extends X11COLOR // #A9A9A9; 169,169,169
    case object `darkkhaki` extends X11COLOR // #BDB76B; 189,183,107
    case object `darkmagenta` extends X11COLOR // #8B008B; 139,0,139
    case object `darkolivegreen` extends X11COLOR // #556B2F; 85,107,47
    case object `darkorange` extends X11COLOR // #FF8C00; 255,140,0
    case object `darkorchid` extends X11COLOR // #9932CC; 153,50,204
    case object `darkred` extends X11COLOR // #8B0000; 139,0,0
    case object `darksalmon` extends X11COLOR // #E9967A; 233,150,122
    case object `darkseagreen` extends X11COLOR // #8FBC8F; 143,188,143
    case object `darkslateblue` extends X11COLOR // #483D8B; 72,61,139
    case object `darkslategray` extends X11COLOR // #2F4F4F; 47,79,79
    case object `darkslategrey` extends X11COLOR // #2F4F4F; 47,79,79
    case object `darkturquoise` extends X11COLOR // #00CED1; 0,206,209
    case object `darkviolet` extends X11COLOR // #9400D3; 148,0,211
    case object `deeppink` extends X11COLOR // #FF1493; 255,20,147
    case object `deepskyblue` extends X11COLOR // #00BFFF; 0,191,255
    case object `dimgray` extends X11COLOR // #696969; 105,105,105
    case object `dimgrey` extends X11COLOR // #696969; 105,105,105
    case object `dodgerblue` extends X11COLOR // #1E90FF; 30,144,255
    case object `firebrick` extends X11COLOR // #B22222; 178,34,34
    case object `floralwhite` extends X11COLOR // #FFFAF0; 255,250,240
    case object `forestgreen` extends X11COLOR // #228B22; 34,139,34
    case object `fuchsia` extends X11COLOR // #FF00FF; 255,0,255
    case object `gainsboro` extends X11COLOR // #DCDCDC; 220,220,220
    case object `ghostwhite` extends X11COLOR // #F8F8FF; 248,248,255
    case object `gold` extends X11COLOR // #FFD700; 255,215,0
    case object `goldenrod` extends X11COLOR // #DAA520; 218,165,32
    case object `gray` extends X11COLOR // #808080; 128,128,128
    case object `green` extends X11COLOR // #008000; 0,128,0
    case object `greenyellow` extends X11COLOR // #ADFF2F; 173,255,47
    case object `grey` extends X11COLOR // #808080; 128,128,128
    case object `honeydew` extends X11COLOR // #F0FFF0; 240,255,240
    case object `hotpink` extends X11COLOR // #FF69B4; 255,105,180
    case object `indianred` extends X11COLOR // #CD5C5C; 205,92,92
    case object `indigo` extends X11COLOR // #4B0082; 75,0,130
    case object `ivory` extends X11COLOR // #FFFFF0; 255,255,240
    case object `khaki` extends X11COLOR // #F0E68C; 240,230,140
    case object `lavender` extends X11COLOR // #E6E6FA; 230,230,250
    case object `lavenderblush` extends X11COLOR // #FFF0F5; 255,240,245
    case object `lawngreen` extends X11COLOR // #7CFC00; 124,252,0
    case object `lemonchiffon` extends X11COLOR // #FFFACD; 255,250,205
    case object `lightblue` extends X11COLOR // #ADD8E6; 173,216,230
    case object `lightcoral` extends X11COLOR // #F08080; 240,128,128
    case object `lightcyan` extends X11COLOR // #E0FFFF; 224,255,255
    case object `lightgoldenrodyellow` extends X11COLOR // #FAFAD2; 250,250,210
    case object `lightgray` extends X11COLOR // #D3D3D3; 211,211,211
    case object `lightgreen` extends X11COLOR // #90EE90; 144,238,144
    case object `lightgrey` extends X11COLOR // #D3D3D3; 211,211,211
    case object `lightpink` extends X11COLOR // #FFB6C1; 255,182,193
    case object `lightsalmon` extends X11COLOR // #FFA07A; 255,160,122
    case object `lightseagreen` extends X11COLOR // #20B2AA; 32,178,170
    case object `lightskyblue` extends X11COLOR // #87CEFA; 135,206,250
    case object `lightslategray` extends X11COLOR // #778899; 119,136,153
    case object `lightslategrey` extends X11COLOR // #778899; 119,136,153
    case object `lightsteelblue` extends X11COLOR // #B0C4DE; 176,196,222
    case object `lightyellow` extends X11COLOR // #FFFFE0; 255,255,224
    case object `lime` extends X11COLOR // #00FF00; 0,255,0
    case object `limegreen` extends X11COLOR // #32CD32; 50,205,50
    case object `linen` extends X11COLOR // #FAF0E6; 250,240,230
    case object `magenta` extends X11COLOR // #FF00FF; 255,0,255
    case object `maroon` extends X11COLOR // #800000; 128,0,0
    case object `mediumaquamarine` extends X11COLOR // #66CDAA; 102,205,170
    case object `mediumblue` extends X11COLOR // #0000CD; 0,0,205
    case object `mediumorchid` extends X11COLOR // #BA55D3; 186,85,211
    case object `mediumpurple` extends X11COLOR // #9370DB; 147,112,219
    case object `mediumseagreen` extends X11COLOR // #3CB371; 60,179,113
    case object `mediumslateblue` extends X11COLOR // #7B68EE; 123,104,238
    case object `mediumspringgreen` extends X11COLOR // #00FA9A; 0,250,154
    case object `mediumturquoise` extends X11COLOR // #48D1CC; 72,209,204
    case object `mediumvioletred` extends X11COLOR // #C71585; 199,21,133
    case object `midnightblue` extends X11COLOR // #191970; 25,25,112
    case object `mintcream` extends X11COLOR // #F5FFFA; 245,255,250
    case object `mistyrose` extends X11COLOR // #FFE4E1; 255,228,225
    case object `moccasin` extends X11COLOR // #FFE4B5; 255,228,181
    case object `navajowhite` extends X11COLOR // #FFDEAD; 255,222,173
    case object `navy` extends X11COLOR // #000080; 0,0,128
    case object `oldlace` extends X11COLOR // #FDF5E6; 253,245,230
    case object `olive` extends X11COLOR // #808000; 128,128,0
    case object `olivedrab` extends X11COLOR // #6B8E23; 107,142,35
    case object `orange` extends X11COLOR // #FFA500; 255,165,0
    case object `orangered` extends X11COLOR // #FF4500; 255,69,0
    case object `orchid` extends X11COLOR // #DA70D6; 218,112,214
    case object `palegoldenrod` extends X11COLOR // #EEE8AA; 238,232,170
    case object `palegreen` extends X11COLOR // #98FB98; 152,251,152
    case object `paleturquoise` extends X11COLOR // #AFEEEE; 175,238,238
    case object `palevioletred` extends X11COLOR // #DB7093; 219,112,147
    case object `papayawhip` extends X11COLOR // #FFEFD5; 255,239,213
    case object `peachpuff` extends X11COLOR // #FFDAB9; 255,218,185
    case object `peru` extends X11COLOR // #CD853F; 205,133,63
    case object `pink` extends X11COLOR // #FFC0CB; 255,192,203
    case object `plum` extends X11COLOR // #DDA0DD; 221,160,221
    case object `powderblue` extends X11COLOR // #B0E0E6; 176,224,230
    case object `purple` extends X11COLOR // #800080; 128,0,128
    case object `red` extends X11COLOR // #FF0000; 255,0,0
    case object `rosybrown` extends X11COLOR // #BC8F8F; 188,143,143
    case object `royalblue` extends X11COLOR // #4169E1; 65,105,225
    case object `saddlebrown` extends X11COLOR // #8B4513; 139,69,19
    case object `salmon` extends X11COLOR // #FA8072; 250,128,114
    case object `sandybrown` extends X11COLOR // #F4A460; 244,164,96
    case object `seagreen` extends X11COLOR // #2E8B57; 46,139,87
    case object `seashell` extends X11COLOR // #FFF5EE; 255,245,238
    case object `sienna` extends X11COLOR // #A0522D; 160,82,45
    case object `silver` extends X11COLOR // #C0C0C0; 192,192,192
    case object `skyblue` extends X11COLOR // #87CEEB; 135,206,235
    case object `slateblue` extends X11COLOR // #6A5ACD; 106,90,205
    case object `slategray` extends X11COLOR // #708090; 112,128,144
    case object `slategrey` extends X11COLOR // #708090; 112,128,144
    case object `snow` extends X11COLOR // #FFFAFA; 255,250,250
    case object `springgreen` extends X11COLOR // #00FF7F; 0,255,127
    case object `steelblue` extends X11COLOR // #4682B4; 70,130,180
    case object `tan` extends X11COLOR // #D2B48C; 210,180,140
    case object `teal` extends X11COLOR // #008080; 0,128,128
    case object `thistle` extends X11COLOR // #D8BFD8; 216,191,216
    case object `tomato` extends X11COLOR // #FF6347; 255,99,71
    case object `turquoise` extends X11COLOR // #40E0D0; 64,224,208
    case object `violet` extends X11COLOR // #EE82EE; 238,130,238
    case object `wheat` extends X11COLOR // #F5DEB3; 245,222,179
    case object `white` extends X11COLOR // #FFFFFF; 255,255,255
    case object `whitesmoke` extends X11COLOR // #F5F5F5; 245,245,245
    case object `yellow` extends X11COLOR // #FFFF00; 255,255,0
    case object `yellowgreen` extends X11COLOR // #9ACD32; 154,205,50
  }  

// ===========================================================================
