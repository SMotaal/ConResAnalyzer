function [rads, noisepow, mpoints]=noisevsf(orig, dith, points)
%NOISEVSF  Spectral noise computation.
%	[FREQ, NDENS] = NOISEVSF(IM1, IM2, POINTS) computes the noise density
%	of image IM1 compared to image IM2.  IM1 and IM2 are related in
%	some way (for instance, IM2 may be a halftoned version of IM1).
%	The difference image is computed, and the noise in POINTS annular
%	regions covering the whole frequency plane is computed.  The
%	vector of noise densities is returned in NDENS, and the vector of
%	center frequencies of the annuli is returned in FREQ.
%
%	See also PSNR,IMG_QI,WSNR,TSNR,LDM.

% From the Image Quality Assessment Software, N. Damera-Venkata and
% Prof. B.L. Evans, released April 28,2001

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

if size(orig) ~= size(dith)
	error('Images must be the same size.'); end

[x,y]=size(orig);				% generate grid for mask
[xplane,yplane]=meshgrid(-x/2+0.5:x/2-0.5);
plane=abs(xplane+i*yplane)/x;

diff=fftshift(fft2(dith-orig));			% noise
diff=diff.*conj(diff);				% power spectrum

noisepow=zeros(1,points);			% snr vector
mpoints=zeros(1,points);			% points vector
hirad=linspace(0.01,1/sqrt(2),points);		% radius vector
lorad=[0 hirad(1:points-1)];
for t=1:points
	mask=(plane>=lorad(t)&plane<hirad(t));	% generate mask
	els=sum(mask(:));			% number of points in mask
	mpoints(t)=els;
	noisepow(t)=sum(sum(diff.*mask))/els;	% noise density
	fprintf('\rComputing... %3d%% done',round(t/points*100))
end
fprintf('\n')
rads=(hirad+lorad)/2;
