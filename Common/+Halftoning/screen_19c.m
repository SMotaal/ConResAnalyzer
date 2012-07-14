function [out] = screen_19c(in)

%SCREEN_19C  Classical screen dithering algorithm, clustered dots.
%	DITH = SCREEN_19C(IM) dithers image IM using a classical unclustered
%	dot screen with 19 graylevels.  The elements of IM must be between
%	0 and 1, where 0 corresponds to black and 1 to white.  DITH is a
%	binary matrix of the same size as IM.
%
%	See also SCREEN_9U, SCREEN_9C, SCREEN_16U, SCREEN_64U.

% Authored by Dr. Tom Kite 1997, modified July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

[y,x] = size(in);
 
in2 = zeros(y+3,x+5); in2(1:y,1:x)=in;
out = zeros(size(in2));
screen = [99 7 8 10 99 99; 6 1 2 13 18 17; 5 4 3 14 15 16; 99 12 11 9 99 99]/19;
 
for b = 1:3:y
  for a = rem(b,6):6:x
    out(b:b+3,a:a+5) = (in2(b:b+3,a:a+5)>=screen) + out(b:b+3,a:a+5);
  end
  %fprintf('\rDithering... %3d%% done',ceil(b/y*100))
end
fprintf('\n')
out=out(1:y,1:x);
