function [y,q,k] = stucki(in)

%STUCKI	Stucki error diffusion.
%	[OUT,Q,K] = STUCKI(IN) error diffuses image IN using the 
%	Stucki error filter.  IN and OUT are between 0 and 1.
%	Q is the error image and K the effective quantizer gain.
%
%	See also STUCKI_SERP, ERRDIFF.

% Authored by Dr. Tom Kite 1997, modified July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


[y,q,k] = errdiff(in,[0 0 -99*42 8 4; 2 4 8 4 2; 1 2 4 2 1]/42);
