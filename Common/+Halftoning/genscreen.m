
function out = genscreen(cell, image, levels)

% OUT = GENSCREEN(CELL,IMAGE,LEVELS) Generate Halftones by ordered dither for 
% a) User defined DITHER MATRIX, b) User defined levels
% Uses a halftone index CELL to halftone a supplied image IM.
%    CELL :   An array of threshold levels.  If these exceed 1, then
%            the cell is scaled to evenly spaced values between 0 and 1.
%            If the numbers all fall between 0 and 1, then they are
%            left as they are.
%    IMAGE:     The image gray levels, values between 0 and 1.
%    LEVELS: The number of levels to use for output display.  
%            Should be 2 (2 corresponds to 0 and 1) or greater.
% OUT: The returned halftone image.  Its values are set to 0
% (white) or 1 (black).  The binary color map is [ 1 1 1; 0 0 0];
% For multiple levels, image can be displayed by using a version of
% imshow that works for multiple levels.
% Calls halftones on each color plane.
%
% SEE ALSO COLORDITHER, runVecFiltOpt, VECDIFF, ColorErrorDiff

% Vishal Monga June 2002, July 2002

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

imred = image(:,:,1);
imblue = image(:,:,3);
imgreen = image(:,:,2);

outred = colordither(cell,imred,levels);
outblue = colordither(cell,imblue,levels);
outgreen = colordither(cell,imgreen,levels);

out(:,:,1) = outred;
out(:,:,2) = outgreen;
out(:,:,3) = outblue;
