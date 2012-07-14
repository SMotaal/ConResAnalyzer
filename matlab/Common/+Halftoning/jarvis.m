function [y,q,k] = jarvis(in)

%JARVIS	Jarvis error diffusion.
%	[OUT,Q,K] = JARVIS(IN) error diffuses image IN using the 
%	Jarvis error filter.  IN and OUT are between 0 and 1.
%	Q is the error image and K the effective quantizer gain.
%
%	See also JARVIS_SERP, ERRDIFF.

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


[y,q,k] = errdiff(in,[0 0 -99*48 7 5; 3 5 7 5 3; 1 3 5 3 1]/48);

% [0 0 -99*48 7 5; 3 5 7 5 3; 1 3 5 3 1]/48 twelve tap jarvis filter -99 set to identify filter origin