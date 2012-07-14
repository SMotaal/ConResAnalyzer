function [out] = screen_9u(in)

%SCREEN_9U  Classical screen dithering algorithm, unclustered dots.
%	DITH = SCREEN_9U(IM) dithers image IM using a classical unclustered
%	dot screen with 9 graylevels.  The elements of IM must be between
%	0 and 1, where 0 corresponds to black and 1 to white.  DITH is a
%	binary matrix of the same size as IM.
%
%	See also SCREEN_9C, SCREEN_16C, SCREEN_19U.

% Authored by Dr. Tom Kite 1997, modified July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

[y,x]=size(in);

in2=zeros(y+4,x+4); in2(3:y+2,3:x+2)=in;
out=zeros(size(in2));
screen=[9 6 4 9; 7 1 8 2; 9 5 3 9]/9;

for b=1:2:y+1
  for a=rem(b,4):4:x+2-rem(b,4)
    out(b:b+2,a:a+3)=(in2(b:b+2,a:a+3)>=screen) + out(b:b+2,a:a+3);
  end
%  fprintf('\rDithering... %3d%% done',ceil(b/(y+1)*100))
end
fprintf('\n')
out=out(3:y+2,3:x+2);
