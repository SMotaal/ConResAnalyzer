function DisplayCMY(cyanIm, magentaIm, yellowIm)

% DisplayCMY Displays a CMY image 
%   DisplayCMY(cyanIm, magentaIm, yellowIm)
% Inputs are the CMY components of the color image
% The valid range for the 3 inputs is [0,1]

% Authored by Vishal Monga, August 2001 

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

redIm = 1 - cyanIm;
greenIm = 1 - magentaIm;
blueIm = 1 - yellowIm;

[imX, mapX] = rgb2ind(redIm, greenIm, blueIm, 256);
imshow(imX, mapX);
