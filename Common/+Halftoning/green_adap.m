function [out,b] = green_adap(in,a,b,h,dir,v)

% GREEN_ADAP  Green Noise Error Diffusion Halftoning 
%  Description
%  [OUT,B] = GREEN_FIXED(IN,A,B,G,DIR,V) performs Green-Noise 
%	error diffusion on image IN using the error filter B, 
%   Hysteresis constant G  and direction DIR i.e 
%   l for raster and -1 for serp. 
%	When V (verbose) is non-zero, progress is printed to the output.
%   Input range is 0 to 1, as is output range.  Input image is modified
%	(no error pipe) for maximum speed.  
%   A is the initial guess for the hysteresis filter. 
%   OUT is the output image matrix, B is the optimum Hysteresis filter 
%   obtained
%
%	See also EDGERRDIFF_L_H, EDGERRDIFF_L, GREEN_FIXED.
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
  b=[0 .6 0;.4 0 0]; end
if nargin<2
  a=[0 .5 0;.5 (-99) 0];end
nu1=0.005;
b1=zeros(size(b));
b2=zeros(size(b));

in = in/255;

[ri,ci]=size(in);
[rm,cm]=size(a);
[r0,c0]=find(a==-99);				% find origin of error filter
fa(r0,c0)=0;

[rmb,cmb]=size(b);
rmb=rmb-1;cmb=cmb-1;

rm=rm-1; cm=cm-1;
inpad=zeros(ri+rm,ci+cm);			% modified input image
inpad(r0:r0+ri-1,c0:c0+ci-1)=in;
%out=zeros(ri,ci); qn=zeros(size(inpad));
%out=-1*ones(size(inpad));qn=-ones(size(inpad));
out=zeros(size(inpad));qn=zeros(size(inpad));
u=zeros(size(inpad));

sp=1; ep=ci; step=1;
r0=r0-1;c0=c0-1;

for y=1:ri
  for x=sp:step:ep
    inpix=inpad(y+r0,x+c0); 

%if(y==64)
 % nu1=.005;
%end

%if(y==384)
 %nu1=.0005;
%end

S=sum(sum(a.*qn(y:y+rm,x:x+cm)));
u_state_pix = inpix - S;
B=sum(sum(b.*out(y:y+rmb,x:x+cmb))); 


if(u_state_pix + h*B >= 0) 
    outpix=1;
 else 
    outpix=0;
 end

%if(abs(u_state_pix + l*inpix)<.2)
% outpix=-outpix;
%end


qerr=(outpix-u_state_pix);
out(y+r0,x+c0)=outpix;

qn(y+r0,x+c0)=qerr;
u(y+r0,x+c0)=u_state_pix + h*B;

tst=(h*B + inpix - S);

if( tst>=0 )
 s=1;
else
 s=0;
end

db= h*(s - inpix)*out(y:y+rmb,x:x+cmb);

b=b-nu1*db;

%for n =1:5

%neg_in_b = find(b<0);
%b(neg_in_b) = zeros(size(neg_in_b));


if((-1)^y < 0)

b(1,1)=0;
b(1,3)=0;
b(2,3)=0;
b(2,2)=0;
b=b + (1-sum(sum(b)))/4;
%b=b/sum(sum(b));
b(1,1)=0;
b(1,3)=0;
b(2,2)=0;
b(2,3)=0;

k1(y,x)=b(2,1);
k2(y,x)=b(1,1);
k3(y,x)=b(1,2);
k4(y,x)=b(1,3);

  else

b(2,2)=0;
b(2,1)=0;
b(1,1)=0;
b(1,3)=0;
b=b + (1-sum(sum(b)))/4;
%b=b/sum(sum(b));
b(1,1)=0;
b(1,3)=0;
b(2,1)=0;
b(2,2)=0;

k1(y,x)=b(2,3);
k2(y,x)=b(1,3);
k3(y,x)=b(1,2);
k4(y,x)=b(1,1);

  end

%For 4-tap

% if((-1)^y < 0)

%b(2,2)=0;
%b(2,3)=0;
%b=b + (1-sum(sum(b)))/4;
%b(2,2)=0;
%b(2,3)=0;
 
% else

%b(2,1)=0;
%b(2,2)=0;
%b=b + (1-sum(sum(b)))/4;
%b(2,1)=0;
%b(2,2)=0;

%  end


%b_ave = (b1+b2)/2;
%b =(1/n)*b + (n/(n+1))*b_ave;
%end



end

%k1=k1';
%k2=k2';
%k1=k1(:);
%k2=k2(:);

if (dir == -1)
   step = -step; temp = ep; ep = sp; sp=temp;
   a=a(:,cm+1:-1:1);
   b=b(:,cmb+1:-1:1);
end

if v

%fprintf('\r Dithering,...% 3d %% done',round(y/ri * 100)),
end
end

if v
 fprintf('\n');
end

k1=k1';
c1 = k1(:);
k2=k2';
c2=k2(:);
k3=k3';
c3=k3(:);
k4=k4';
c4=k4(:);

out = out(r0:r0+ri-1,c0:c0+ci-1);





