function [rad, snr]=tsnr(orig, dith, target)
%TSNR  Target signal to noise ratio.
%	[FREQ, SNR] = TSNR(IM1, IM2, TARGET) finds the radial frequency
%	cutoff, FREQ, and the associated signal-to-noise ratio, SNR,
%	for which the signal-to-noise ratio between IM1 and IM2 is
%	approximately equal to TARGET.  IM1 and IM2 are related in some
%	way (for instance, IM2 may be a halftoned version of IM1).  A
%	circular low-pass mask is applied to both images, and the signal
%	to noise ratio, where IM1 is the signal and (IM2-IM1) is the
%	noise, is computed.  The cutoff frequency of the mask is adjusted
%	until the SNR is approximately equal to TARGET (in dB).
%
%	Because a successive binary approximation method is used to find
%	the cutoff frequency, severely non-monotonic noise powers with
%	frequency may give unpredictable results.
%
%	See also SNRVSF, SNR, PSNR, WSNR.

% From the Image Quality Assessment Software, N. Damera-Venkata and 
% Prof. Brian L. Evans, released 28th April 2001

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

if size(orig) ~= size(dith)
  error('Images must be the same size.');
end

[x,y]=size(orig);				% generate grid for mask
[xplane,yplane]=meshgrid(-x/2+0.5:x/2-0.5);
plane=abs(xplane+i*yplane)/x;

diff=fftshift(fft2(dith-orig));			% noise
orig=fftshift(fft2(orig));			% signal

diff=diff.*conj(diff);				% power spectra
orig=orig.*conj(orig);

radmin=0; radmax=1/sqrt(2); rad=radmax/2; snr=0; oldsnr=1;

while (oldsnr ~= snr)				% stop after a repeat
  mask=plane<rad;				% generate mask
  origpow=sum(sum(orig.*mask));			% passband signal power
  diffpow=sum(sum(diff.*mask));			% passband noise power
  if diffpow==0
    error('Unachievable SNR with given images.');
  end
  oldsnr=snr; snr=10*log10(origpow/diffpow);
  if (snr>target)
    newrad=(rad+radmax)/2;			% go halfway to prev. max
    radmin=rad;
  else
    newrad=(rad+radmin)/2;			% go halfway to prev. min
    radmax=rad;
  end
  rad=newrad;
end
