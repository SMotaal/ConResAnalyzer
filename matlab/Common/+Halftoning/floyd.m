function [y,q,k] = floyd(in)

%FLOYD	Floyd-Steinberg error diffusion.
%	[OUT,Q,K] = FLOYD(IN) error diffuses image IN using the 
%	Floyd-Steinberg error filter.  IN and OUT are between 0 and 1.
%	Q is the error image and K the effective quantizer gain.
%
%	See also FLOYD_SERP, ERRDIFF.

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

[y,q,k] = errdiff(in);
