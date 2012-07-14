function out = green_fixed(in,a,b,h,dir,v)

% GREEN_FIXED  Green Noise Error Diffusion Halftoning 
%  Description
%  OUT = GREEN_FIXED(IN,A,B,G,DIR,V) performs Green-Noise 
%	error diffusion on image IN using the error filter B, 
%   fixed hysteresis filter A, Hysteresis constant G 
%	and direction DIR l for raster and -1 for serp. 
%	When V (verbose) is non-zero, progress is printed to the output.
%   Input range is 0 to 1, as is output range.  Input image is modified
%	(no error pipe) for maximum speed.  
%
%	See also EDGERRDIFF_L_H, EDGERRDIFF_L, GREEN_ADAP.
%
%	Ref: 
%   1.) D.L. Lau, G.R. Arce and N.C. Gallagher, "Green Noise
%   Digital Halftoning", Proceedings of the IEEE 
%   Vol. 86, pp 2424-2442, December 1998. 
%   2.) N. Damera-Venkata and Evans,"Adaptive Threshold Modulation for 
%   Error Diffusion Halftoning", IEEE Transactions on Image Processing, 
%   Vol. 10, No. 1, January 2001 

% Authored July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


if nargin<6					% default to verbose
  v=1; end
if nargin<5					% default to raster
  dir=1; end
if nargin<4					% default to unmodified
  h=0; end
if nargin<3					% default to standard weights
  b=[0 .6 0; .4 0 0]; end
if nargin<2
  a=[0 .5 0;.5 (-99) 0];end

in = in/255;

[ri,ci]=size(in);
[rm,cm]=size(a);
[r0,c0]=find(a==-99);				% find origin of error filter
fa(r0,c0)=0;

%[rmb,cmb]=size(b);
%rmb=rmb-1;cmb=cmb-1;

rm=rm-1; cm=cm-1;
inpad=zeros(ri+rm,ci+cm);			% modified input image
inpad(r0:r0+ri-1,c0:c0+ci-1)=in;
out=zeros(size(inpad));qn=zeros(size(inpad));
u=zeros(size(inpad));

sp=1; ep=ci; step=1;
r0=r0-1;c0=c0-1;

for y=1:ri
  for x=sp:step:ep
    inpix=inpad(y+r0,x+c0); 

S=sum(sum(a.*qn(y:y+rm,x:x+cm)));
u_state_pix = inpix - S;
B=sum(sum(b.*out(y:y+rm,x:x+cm))); 


if(u_state_pix + h*B >= 0) 
    outpix=1;
 else 
    outpix=0;
 end

qerr=(outpix-u_state_pix);
out(y+r0,x+c0)=outpix;

qn(y+r0,x+c0)=qerr;
u(y+r0,x+c0)=u_state_pix + h*B;


end

if (dir == -1)
   step = -step; temp = ep; ep = sp; sp=temp;
   a=a(:,cm+1:-1:1);
   b=b(:,cm+1:-1:1);
end

if v

%fprintf('\r Dithering,...% 3d %% done',round(y/ri * 100)),
end
end

if v
 fprintf('\n');
end

out = out(r0:r0+ri-1,c0:c0+ci-1);
