function [out,qn,L_adap,fc,u,K] = edgerrdiff_l_h(in,fc,l,dir,v,dbf)

% EDGERRDIFF_L_H Optimum Edge Enhancement Error Diffusion Halftoning by 
%  adapting sharpness parameter L and error filter H
%  Description
%  [OUT,QN,L_ADAP,H_adap,u,K] = EDGERRDIFF_L_H(IN,FC,l,DIR,V,DBF,Nu) performs edge enhanced 
%	error diffusion on image IN. 
%  Same as EDGERRDIFF_L except that it adapts the error filter as well and uses 
%  the optimum error filter obtained on the result of the adaptation algorithm.
%  DIR,V,DBF,Nu are same as in EDGERRDIFF_L
%  FC is the initial guess for the error filter (Default Floyd-Steinberg)
%  H_adap is the optimal error filter obtained.
%  u is the Quantizer input image in a vector format, K is the optimum scalar gain 
%  for grayscale error diffusion halftoning
%
%	See also EDGERRDIFF_L, EDGERRFIXED, DISP_ERR
%
%	Ref: 
%   1.) R. Eschbach and K. Knox, "Error diffusion algorithm with
%	edge enhancement", J. Opt. Soc. Am. A, Vol. 8, No. 12, December
%	1991, pp. 1844-1850.
%   2.) N. Damera-Venkata and Evans,"Adaptive Threshold Modulation for 
%   Error Diffusion Halftoning", IEEE Transactions on Image Processing, 
%   Vol. 10, No. 1, January 2001 

% Authored 2001 by N. Damera-Venkata, modified July 2002 by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


if nargin<6
   dbf = 0; end
if nargin<5					% default to verbose
  v=1; end
if nargin<4					% default to raster
  dir=1; end
if nargin<3					% default to unmodified
  l=0; end
if nargin<2					% default to Floyd-Steinberg
   fc=[1 5 3; 7 (-99*16) 0]/16; end

in = (in/255)*2-1;              %scale the input to correct range
%in = (in/255); %for [0,1]

[ri,ci]=size(in);
[rm,cm]=size(fc);
[r0,c0]=find(fc==-99);				% find origin of error filter
fc(r0,c0)=0;

rm=rm-1; cm=cm-1;
inpad=zeros(ri+rm,ci+cm);			% modified input image
inpad(r0:r0+ri-1,c0:c0+ci-1)=in;
out=zeros(ri,ci); qn=zeros(size(inpad));
%out=-1*ones(ri,ci);qn=-1*ones(size(inpad));
u=zeros(size(inpad));

sp=1; ep=ci; step=1;
r0=r0-1;c0=c0-1;

for y=1:ri
  for x=sp:step:ep
    inpix=inpad(y+r0,x+c0); 

S=sum(sum(fc.*qn(y:y+rm,x:x+cm)));
u_state_pix = inpix - S;
 
 %if(u_state_pix + l*inpix >= .5) 
 %   outpix=1;
 %else 
 %   outpix=0;
 %end

if(u_state_pix + l*inpix >= 0) 
    outpix=1;
 else 
    outpix=-1;
 end
 
if (dbf ~= 0)
	if(abs(u_state_pix + l*inpix)< dbf)
 		outpix=-outpix;
    end
end

L(y,x) = l;

qerr=outpix-u_state_pix;
out(y,x)=outpix;

qn(y+r0,x+c0)=qerr;
u(y+r0,x+c0)=u_state_pix;

%dh = -2*qn(y:y+rm,x:x+cm)*(qerr-S);
%fc=fc-.05*dh;
%fc(2,2)=0;
%fc(2,3)=0;
%fc=fc + (1-sum(sum(fc)))/4;
%fc(2,2)=0;
%fc(2,3)=0;

end

if (dir == -1)
   step = -step; temp = ep; ep = sp; sp=temp;
   fc=fc(:,cm+1:-1:1);
end

if v

%fprintf('\r Dithering,...% 3d %% done',round(y/ri * 100)),
end
end

if v
 fprintf('\n');
end

qn = qn(r0:r0+ri-1,c0:c0+ci-1);
u = out(:) - qn(:);
a= find(abs(u)<=1);
u_k = (u(a)- mean(mean(u(a))));
out_k = out(a)- mean(mean(out(a)));
K =sum(sum(u_k.*out_k))/(sum(sum(u_k.^2)));
L = L';
L_adap = L(:);






