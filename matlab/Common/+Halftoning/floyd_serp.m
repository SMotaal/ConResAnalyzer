function [y,q,k] = floyd_serp(in)

%FLOYD_SERP Floyd-Steinberg error diffusion, serpentine scan.
%	[OUT,Q,K] = FLOYD(IN) error diffuses image IN using the 
%	Floyd-Steinberg error filter.  IN and OUT are between 0 and 1.
%	Q is the error image and K the effective quantizer gain.
%
%	See also FLOYD, ERRDIFF.

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


[y,q,k] = errdiff(in,[0 -99*16 7; 3 5 1]/16,0,-1); %FS but param -1 for Serpentine Scan
