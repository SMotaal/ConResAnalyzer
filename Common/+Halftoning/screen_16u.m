
function [out] = screen_16u(in)

%SCREEN_16U  Classical screen dithering algorithm, dispersed (unclustered) dots.
%	DITH = SCREEN_16U(IM) dithers image IM using a classical unclustered
%	dot screen with 16 graylevels.  The elements of IM must be between
%	0 and 1, where 0 corresponds to black and 1 to white.  DITH is a
%	binary matrix of the same size as IM.
%
%	See also SCREEN_9U, SCREEN_9C, SCREEN_19C.

% Authored by Dr. Tom Kite 1997, modified July 2002 by Vishal Monga 

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

[y,x] = size(in);
 
in2 = zeros(y+3,x+3); in2(1:y,1:x) = in;
out = zeros(size(in2));
% screen=[6 11 7 10; 14 1 15 4; 8 9 5 12; 16 3 13 2]/17;
screen = [8 11 7 10; 15 1 16 4; 6 9 5 12; 13 3 14 2]/17;
 
for b = 1:4:y
  for a = 1:4:x
    out(b:b+3,a:a+3) = (in2(b:b+3,a:a+3)>=screen) + out(b:b+3,a:a+3);
  end
  %fprintf('\rDithering... %3d%% done',ceil(b/y*100))
end
fprintf('\n')
out = out(1:y,1:x);
