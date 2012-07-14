
%  Function to convert frame buffer RGB to linear RGB according to a standard sRGB monitor.
%   OUT = CRT_CHAR(IN) 
%  Converts the input scalar/vector/image from frame-buffer value to linear value for use
%  in image processing software.
%  Input range is 0 to 255, as is the output range. 
%  The program returns the output OUT which may be regarded as the gamma-uncorrected version of IN.
%
%  example: (here in and out are RGB images)
%  out = crt_char(in); uses a standard sRGB crt characteristic on the frame-buffer image in, and ret
%  returns a linear RGB image out.

%  Authored Niranjan-Damera Venkata, Dec 2000
%  Modified July 2001, Vishal Monga 

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function out = crt_char(in)

in = in/255;

%%%sRGB%%%%%%%%
b=0.03928;
a=0.055;
g=2.4;
M=12.92;
%%%%%%%%%%%%%%%

o=(in <= b);
out = (o.*in)/M + (1-o).*(((in+ a)/(1+a)).^g);

out = clip(round(255*out),0,255);



