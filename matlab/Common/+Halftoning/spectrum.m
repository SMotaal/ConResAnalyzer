% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

a = imread('barbara.tif');

res = fftshift(fft2(a));
final = log(1 + abs(res));
final = final/(max(max(final)));
imwrite(final,'original_spectrum.tif');
imshow(final)

