function W = clip(X,a,b)
% CLIP Clip the entries in one image wrt a comparison image 
% W = CLIP(X,a,b)
% a,b: can be either scalars or imges of same size as X

% Authored August 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

W = max(a,min(b,X));