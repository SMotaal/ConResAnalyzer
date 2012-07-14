% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

%%%Script to run and test Color Halftones

% set up Floyd-Steinberg error pattern ...

FS_3 =	[0 0 7;  ...
	     3 5 1];
FS_3 = FS_3 / sum(FS_3(:));

% and Beyer's "optimum dot pattern"

Bayer_Mat = ...
		[1 17 5 21 2 18 6 22;
		25 9 29 13 26 10 30 14;
		7 23 3 19 8 24 4 20;
		31 15 27 11 32 16 28 12;
		2 18 6 22 1 17 5 21;
		26 10 30 14 25 9 29 13;
		8 24 4 20 7 23 3 19;
		32 16 28 12 31 15 27 11];

 halfToneCell_B4 = Bayer_Mat/32;   
    
%halfToneCell_B4 = [8 11 7 10; 15 1 16 4; 6 9 5 12; 13 3 14 2]/17;

% either of these halftoning algorithms can be used with separate color
% channels to achieve acceptable color halftoning results.

% We will examine a multi-level halftoning algorithm to estimate the
% effects of having variable drop levels or multiple ink weights
% for a printer.

% The function looks like this ...
%
% mlHalfToneImage(halfToneCell, testImage, N)
%
% ... where halfToneCall is a square dither array, such as clustered dot
% or the Meyer "optimal" pattern, testImage is the continuous tone image
% data to be halftoned, and N represents the desired number of levels 
% for the halftone output.


% We will also experiment with a color-specific Color Error Diffusion
% technique sometimes referred to as vector diffusion.  It can be called
% as follows ...
%
% ColorErrorDiff(FS_3, redIm, greenIm, blueIm)
%
% Where FS_3 is a valid error diffusion matrix, such as the one used
% with Floyd-Steinberg, and redIm, greenIm, and blueIm are the 
% separated red, green, and blue values ranging from 0 to 1 to be
% combined.  Note that this fuction must use all three at once.
% To simplify paramter parsing, ColorErrorDiff prints it's results
% directly as well as passing back a single variable with all color 
% channel information.

% -----------------------------------------------------------------------
% Start with several color ramps ...
% -----------------------------------------------------------------------

%rampWidth = 400
%rampHeight = 60

%imZeros = zeros(rampHeight, rampWidth);
%imOnes = ones(rampHeight, rampWidth);

%imRamp = zeros(rampHeight, rampWidth);

%for y = [1:1:rampHeight]
%    for x = [1:1:rampWidth]
%        imRamp(y,x) = x/rampWidth;
%    end
%end

% create a green ramp from cmy arrays

%cyanIm = imRamp;
%magentaIm = imZeros;
%yellowIm = imRamp;

img = imread('lena.tif');
IM = double(img)/255;

cyanIm = 1 - IM(:,:,1);
magentaIm = 1 - IM(:,:,2);
yellowIm = 1 - IM(:,:,3);


% Display using DisplayCMY function
figure;
DisplayCMY(cyanIm, magentaIm, yellowIm);   

% now show results of separate ED ...
%cyanIm_ht1 = FloydSteinberg(FS_3, cyanIm);
%magentaIm_ht1 = FloydSteinberg(FS_3, magentaIm);
%yellowIm_ht1 = FloydSteinberg(FS_3, yellowIm);
%figure;
%DisplayCMY(cyanIm_ht1, magentaIm_ht1, yellowIm_ht1);

% now show results of combined Color Error Diffusion ...
  
redIm = 1 - cyanIm;
greenIm = 1 - magentaIm;
blueIm = 1 - yellowIm;
figure;
ret = ColorErrorDiff(FS_3, redIm, greenIm, blueIm);

% note that the cyan and magenta elements have been separated
% yet blended to achieve the same overall color levels.

% Now try a Beyer dither matrix ...

%HT_r = HalfToneImage(halfToneCell_B4, redIm);
%HT_b = HalfToneImage(halfToneCell_B4, blueIm);
%HT_g = HalfToneImage(halfToneCell_B4, greenIm);
%figure;
%imshow(HT_r, HT_g, HT_b);

% now try multiple levels of color ...
%mlHT_r = mlHalfToneImage(halfToneCell_B4, redIm, 1);
%mlHT_b = mlHalfToneImage(halfToneCell_B4, blueIm, 1);
%mlHT_g = mlHalfToneImage(halfToneCell_B4, greenIm, 1);

%mlHT_r = colordither(halfToneCell_B4, redIm, 2);
%mlHT_b = colordither(halfToneCell_B4, blueIm, 2);
%mlHT_g = colordither(halfToneCell_B4, greenIm, 2);

%result = genscreen(halfToneCell_B4, IM, 2);

%mlHT_r = result(:,:,1);
%mlHT_b = result(:,:,2);
%mlHT_g = result(:,:,3);

%figure;
%imshow(mlHT_r, mlHT_g, mlHT_b)

% the extra level of green (by way of light cyan and yellow, 
% if you like) really makes the simple Bayer matrix competitive
