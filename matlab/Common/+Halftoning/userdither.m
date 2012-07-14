function htimage = colordither(cell, im, levels)

% HTIMAGE = COLORDITHER(CELL,IM,LEVELS) Generate Halftones by ordered dither for 
% a) User defined DITHER MATRIX, b) User defined levels
% Uses a halftone index CELL to halftone a supplied image IM
% over multiple levels of intensity vs. the default 1 and 0 levels.
% Intensity levels are divided evenly among the levels passed 
% to the function.
%    CELL :   An array of threshold levels.  If these exceed 1, then
%            the cell is scaled to evenly spaced values between 0 and 1.
%            If the numbers all fall between 0 and 1, then they are
%            left as they are.
%    IM:     The image gray levels, values between 0 and 1.
%    LEVELS: The number of levels to use for output display.  
%            Should be 2 (2 corresponds to 0 and 1) or greater.
% HTIMAGE: The returned halftone image.  Its values are set to 0
% (white) or 1 (black).  The binary color map is [ 1 1 1; 0 0 0];
% For multiple levels, image can be displayed by using a version of
% imshow that works for multiple levels.   



% Authored: Vishal Monga June 2002, July 2002

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


% Extracting Key Parameters 
imSize = size(im);
cellSize = size(cell);
levels = levels - 1;

% The cell thresholds should fall at the midpoints of the level divisions
% mdeleon note: mmax might be a psych221 specific function ... ?
% 
if max(cell) > 1 
  low = (1/mmax(cell))*0.5; high = 1 - low;
  halfToneCell = scale(cell,low,high);
else
  halfToneCell = cell;
end

% check to make sure levels >= 2 ...
if (levels < 1)
    disp('Error: mlHalfToneImage should use at least 2 levels of output.')
end

% Determine number of halftone cells needed to cover the image

rc = imSize ./ cellSize;
r = ceil(rc(1));
c = ceil(rc(2));

% Builds an image that covers the original and whose entries
% contain the values of the halfToneCell repeated, again and again. 
halfToneMask = kron(ones(r, c), halfToneCell);

% Crop out that part of the mask equal in size to the image. 
halfToneMask = halfToneMask(1:imSize(1), 1:imSize(2));

htimage = zeros(1:imSize(1), 1:imSize(2));

% Iterate compares based on a number of threshold values equal to the
% specified levels.

for I = 1:levels
    levelMask = ((I-1)/levels) + halfToneMask/levels;
    htStep = (levelMask < im);
    htimage = htimage + htStep;
end
htimage = htimage / levels;

return;

