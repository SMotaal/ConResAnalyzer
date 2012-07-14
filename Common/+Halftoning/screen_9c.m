function [out] = screen_9c(in)

%SCREEN_9C  Classical screen dithering algorithm, clustered dots.
%	DITH = SCREEN_9C(IM) dithers image IM using a classical clustered
%	dot screen with 9 graylevels.  The elements of IM must be between
%	0 and 1, where 0 corresponds to black and 1 to white.  DITH is a
%	binary matrix of the same size as IM.
%
%	See also SCREEN_9U, SCREEN_16C, SCREEN_19U.

% Authored by Dr. Tom Kite 1997, modified July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


[y,x] = size(in);

in2 = zeros(y+4,x+4); in2(3:y+2,3:x+2)=in;
out = zeros(size(in2));
screen = [20 9 11 20; 13 1 3 15; 20 7 5 20]/16;

for b = 1:2:y+1
  for a = rem(b,4):4:x+2-rem(b,4)
    out(b:b+2,a:a+3) = (in2(b:b+2,a:a+3)>=screen) + out(b:b+2,a:a+3);
  end
%  fprintf('\rDithering... %3d%% done',ceil(b/(y+1)*100))
end
fprintf('\n')
out=out(3:y+2,3:x+2);
