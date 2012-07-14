function ratio=psnr(orig, dith, maxgray)

%PSNR  Peak signal to noise ratio.
%	RATIO = PSNR(IM1, IM2) computes the peak signal to noise ratio
%	of IM2 with respect to IM1 and returns the result in dB.  The peak
%	graylevel is assumed to be 255 unless specified otherwise as a
%	third argument to PSNR.
%
%	See also WSNR, TSNR, IMG_QI, TSNR

% From the Image Quality Assessment Software, N. Damera-Venkata and
% Prof. B.L. Evans, released April 28,2001

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

if size(orig) ~= size(dith)
  error('Images must be the same size.');
end

if nargin==2
  maxgray=255;
end

mse=sum((orig(:)-dith(:)).^2);
ratio=10*log10(size(orig,1)*size(orig,2)*maxgray^2/mse);
