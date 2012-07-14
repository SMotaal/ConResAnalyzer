function [out] = bnoise(in)

%BNOISE 'Blue noise' dithering algorithm.
%	DITH = BNOISE(IM) dithers image IM using blue noise.  The blue 
%	noise is generated from white noise, filtered using a filter
%	whose response rises sharply in the high frequencies (especially 
%	the diagonals).  The elements of IM must be between 0 and 1, where
%	0 corresponds to black and 1 to white.  DITH is a binary matrix
%	of the same size as IM.
%
%	Ref: T. Mitsa and K. Parker, "Digital Halftoning using a blue
%   noise mask", J. Opt. Soc. Am. A, Vol. 9, November
%	1992, pp. 1920-1929.

% This file was authored by Dr. Thomas Kite 1997, now at Audio Precision, Beaverton.
% and modified by Vishal Monga, July 2002.

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

[iy,ix] = size(in);
n = rand(iy,ix)-0.5;				% white noise

%global bnoisefilter
%global glmap

if (~exist('bnoisefilter'))			% only design filter once
	[x,y]=meshgrid(-10:10);
	z=fftshift(abs(x+i*y));
	z=z/max(z(:));
	f=sin(z*pi/2).^8;			% desired frequency response
	g=fftshift(real(ifft2(f)));
	h=g(9:13,9:13);
	bnoisefilter=(h-mean(h(:)))*2.5;	% resultant filter
	end
    
if (~exist('glmap'))				% non-linear predistortion
	load bnoisepredist; 
end

nf = conv2(n,bnoisefilter,'same');		% get blue from white
f  =  find(nf>0.5); nf(f)=0.45*ones(size(f));	% clip for good whites
f  =  find(nf<-0.5); nf(f)=-0.45*ones(size(f));	% clip for good blacks

inmapped = reshape(glmap(round(in*255 + 1)),iy,ix);	% predistort
out = (inmapped + nf)>0.5;				% dither 
