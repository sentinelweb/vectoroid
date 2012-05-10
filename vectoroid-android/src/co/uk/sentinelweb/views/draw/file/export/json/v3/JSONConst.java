package co.uk.sentinelweb.views.draw.file.export.json.v3;
/*
Vectoroid API for Andorid
Copyright (C) 2010-12 Sentinel Web Technologies Ltd
All rights reserved.
 
This software is made available under a Dual Licence:
 
Use is permitted under LGPL terms for Non-commercial projects
  
see: LICENCE.txt for more information

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Sentinel Web Technologies Ltd. nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SENTINEL WEB TECHNOLOGIES LTD BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
public class JSONConst {
	public static float version = 3;
	
	// point
	public static final String JSON_Y = "y";
	public static final String JSON_X = "x";
	
	//shared
	public static final String JSON_ID = "id";
	
	//drawing
	public static final String JSON_SIZE = "size";
	public static final String JSON_VERSION = "version";
	public static final String JSON_DENSITY = "density";
	public static final String JSON_BG = "background";
	public static final String JSON_ELEMENTS = "elements";
	public static final String JSON_LAYERS = "layers";
	
	// element type
	public static final String JSON_EL_TYPE = "elementType";
	public static final String JSON_EL_TYPE_STROKE = "stroke";
	public static final String JSON_EL_TYPE_LAYER = "layer";
	public static final String JSON_EL_TYPE_GROUP = "group";
	
	//fill types
	public static final String JSON_TYPE = "type";
	public static final String JSON_COLOUR = "colour";
	public static final String JSON_WEBCOLOUR = "webcolour";
	public static final String JSON_GRADIENT = "gradient";
	public static final String JSON_ASSET = "asset";
	public static final String JSON_BMP_ALPHA = "bmpAlpha";
	
	// drawing element 
	public static final String JSON_LOCKED = "locked";
	public static final String JSON_VISIBLE = "visible";
	
	// stroke
	public static final String JSON_TEXT = "text";
	public static final String JSON_TEXTXSCALE = "textXScale";
	public static final String JSON_FONT_NAME = "fontName";
	public static final String JSON_POINTS = "points";
	public static final String JSON_PEN = "pen";
	public static final String JSON_FILL = "fill";

	//public static final String JSON_POINTS = "points";
	// pointvec
	public static final String JSON_CLOSED = "closed";
	public static final String JSON_ISHOLE = "isHole";
	public static final String JSON_D = "d";
	//public static final String JSON_POINTSSTR = "pointsStr";
	//public static final String JSON_PRESSURE = "pressure";
	public static final String JSON_PRESSURE = "pressure";
	//public static final String JSON_BIEZER1 = "biezer1";
	//public static final String JSON_BIEZER1STR = "biezer1Str";
	//public static final String JSON_BIEZER2 = "biezer2";
	//public static final String JSON_BIEZER2STR = "biezer2Str";
	//public static final String JSON_TIME = "time";
	public static final String JSON_TIMEDELTA = "timeDelta";
	public static final String JSON_STARTTIME = "startTime";
	
	// pen
	public static final String JSON_GLO_COL = "glowColour";
	public static final String JSON_STR_COL = "strokeColour";
	public static final String JSON_GLO_WID = "glowWidth";
	public static final String JSON_STR_WID = "strokeWidth";
	public static final String JSON_GLO_OFFSET = "glowOffset";
	public static final String JSON_SCALE_PEN = "scalePen";
	public static final String JSON_EMB_RADIUS = "embRadius";
	public static final String JSON_EMB_SPECULAR = "embSpecular";
	public static final String JSON_EMB_AMBIENT = "embAmbient";
	public static final String JSON_EMB_ENABLE = "embEnable";
	public static final String JSON_ROUNDING = "rounding";
	public static final String JSON_CAP = "cap";
	public static final String JSON_JOIN = "join";
	public static final String JSON_START_TIP = "startTip";
	public static final String JSON_END_TIP = "endTip";
	public static final String JSON_BREAK_TYPE = "breakType";
	//private static final String JSON_TYPE = "type";
	
	// gradient
	public static final String JSON_COLOURS = "colours";
	public static final String JSON_POSITIONS = "positions";
	public static final String JSON_GD = "gradientData";
	
	// gradient data
	public static final String JSON_GD_P1 = "p1";
	public static final String JSON_GD_P2 = "p2";
	
	// asset
	public static final String JSON_NAME = "name";
	public static final String JSON_DATA = "data";
	//private static final String JSON_TYPE = "type";
	//private static final String JSON_CLOSED = "closed";
	public static final String JSON_FILLED = "filled";
	public static final String JSON_INSIDE = "inside";

}
