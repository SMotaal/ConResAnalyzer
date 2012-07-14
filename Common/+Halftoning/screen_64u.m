function [out] = screen_64u(in)

%SCREEN_64U  Classical screen dithering algorithm, dispersed (unclustered) dots.
%	DITH = SCREEN_64U(IM) dithers image IM using a classical unclustered
%	dot screen with 64 graylevels.  The elements of IM must be between
%	0 and 1, where 0 corresponds to black and 1 to white.  DITH is a
%	binary matrix of the same size as IM.
%
%	See also SCREEN_9U, SCREEN_9C, SCREEN_16U, SCREEN_19C.

% Authored by Dr. Tom Kite 1997, modified July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

[y,x] = size(in);
 
in2 = zeros(y+3,x+3); in2(1:y,1:x)=in;
out = zeros(size(in2));
screen = zeros(8);
screen_sub = [8 11 7 10; 15 1 16 4; 6 9 5 12; 13 3 14 2];
screen(1:2:7,1:2:7) = screen_sub;
screen(2:2:8,2:2:8) = screen_sub+16;
screen(2:2:8,1:2:7) = screen_sub+32;
screen(1:2:7,2:2:8) = screen_sub+48;

screen = screen/65; %To make sum of row/column elements <= 1

for b = 1:8:y
  for a = 1:8:x
    out(b:b+7,a:a+7)=(in2(b:b+7,a:a+7)>=screen) + out(b:b+7,a:a+7);
  end
  %fprintf('\rDithering... %3d%% done',ceil(b/y*100))
end
fprintf('\n')
out = out(1:y,1:x);
