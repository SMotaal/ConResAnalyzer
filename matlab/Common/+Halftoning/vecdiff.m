
% Function to do vector color error diffusion
%   [OUT_VEC,QN_VEC,K_vec] = vecdiff(IN,DIR,L,D,F,V,CENTPIX)
%   performs vector error diffusion on the input image IN -- M x N x 3
%   using direction DIR (default to unmodified i.e. dir = 1), scan type L 
%   l denotes the raster scan and -l serpentine scan and matrix valued 
%   error filter F. The default values for V are verbose (v = 0) and CENTPIX = [2 2]' 
%   The  order of filter coefficients returned in F and OF is [(0,1) (1,1) (1,0) (1,-1)]
%   This code is run typically after computing F using runVecFiltOpt
%  example: 
%  [F,OF] = runVecFiltOpt(7);
%  [out_vec,qn_vec,K_vec] = vecdiff(in,1,l,0,F);
% 
%  SEE ALSO runVecFiltOpt, CRT_CHAR, RES_CORR, COLOR_MODEL 

%  Authhored by Niranjan Damera-Venakta December 2000 
%  Modified July 2002, Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function [out,qn,K] = vecdiff(in,dir,l,d,F,v,centpix)

if nargin<7
   centpix = [2 2]'; end
if nargin<6					% default to verbose
  v=0; end
if nargin<3					% default to raster
  l=[0 0 0;0 0 0;0 0 0]; end 
if nargin<2					% default to unmodified
   dir=1; end
if nargin<5					% default to Floyd-Steinberg
   f1=[7 0 0;0 7 0;0 0 7]/16;
   f2=[1 0 0;0 1 0;0 0 1]/16;
   f3=[5 0 0;0 5 0;0 0 5]/16;
   f4=[3 0 0;0 3 0;0 0 3]/16;
   F = [f1 f2 f3 f4];
end
if nargin <4
    d=1;
end    
    

in = (in/255);              %scale the input to correct range

num = zeros(3);
den = zeros(3);
u_mean = zeros(3,1);
out_mean = zeros(3,1);

f1 = F(:,1:3);
f2 = F(:,4:6);
f3 = F(:,7:9);
f4 = F(:,10:12);

f11 = [f2(1,1) f3(1,1) f4(1,1);f1(1,1) 0 0];
f12 = [f2(1,2) f3(1,2) f4(1,2);f1(1,2) 0 0];
f13 = [f2(1,3) f3(1,3) f4(1,3);f1(1,3) 0 0];
f21 = [f2(2,1) f3(2,1) f4(2,1);f1(2,1) 0 0];
f22 = [f2(2,2) f3(2,2) f4(2,2);f1(2,2) 0 0];
f23 = [f2(2,3) f3(2,3) f4(2,3);f1(2,3) 0 0];
f31 = [f2(3,1) f3(3,1) f4(3,1);f1(3,1) 0 0];
f32 = [f2(3,2) f3(3,2) f4(3,2);f1(3,2) 0 0];
f33 = [f2(3,3) f3(3,3) f4(3,3);f1(3,3) 0 0];


[ri,ci]=size(in(:,:,1));
[rm,cm]=size(f11);
r0 = centpix(1);
c0 = centpix(2);


rm=rm-1; cm=cm-1;
inpad=zeros(ri+rm,ci+cm,3);			% modified input image
inpad(r0:r0+ri-1,c0:c0+ci-1,1)=in(:,:,1);
inpad(r0:r0+ri-1,c0:c0+ci-1,2)=in(:,:,2);
inpad(r0:r0+ri-1,c0:c0+ci-1,3)=in(:,:,3);

inpix = [0 0 0]';
S = [0 0 0]';

out=zeros(ri,ci,3); qn=zeros(size(inpad));


sp=1; ep=ci; step=1;
r0=r0-1;c0=c0-1;
qerr=[0 0 0]';
for y=1:ri
   
  for x=sp:step:ep
     inpix(1)=inpad(y+r0,x+c0,1);
     inpix(2)=inpad(y+r0,x+c0,2);
     inpix(3)=inpad(y+r0,x+c0,3);
     
if(d==0)     
   S(1)=sum(sum(f11.*qn(y:y+rm,x:x+cm,1))) + sum(sum(f12.*qn(y:y+rm,x:x+cm,2))) + sum(sum(f13.*qn(y:y+rm,x:x+cm,3)));
   S(2)=sum(sum(f21.*qn(y:y+rm,x:x+cm,1))) + sum(sum(f22.*qn(y:y+rm,x:x+cm,2))) + sum(sum(f23.*qn(y:y+rm,x:x+cm,3)));
   S(3)=sum(sum(f31.*qn(y:y+rm,x:x+cm,1))) + sum(sum(f32.*qn(y:y+rm,x:x+cm,2))) + sum(sum(f33.*qn(y:y+rm,x:x+cm,3)));
else
   S(1)=sum(sum(f11.*qn(y:y+rm,x:x+cm,1)));
   S(2)=sum(sum(f22.*qn(y:y+rm,x:x+cm,2)));
   S(3)=sum(sum(f33.*qn(y:y+rm,x:x+cm,3)));
end   
u_state_pix = inpix - S;

outpix=zeros(size(u_state_pix));
 
  outpix(1) = ( u_state_pix(1) >= 0.5  );
  outpix(2) = ( u_state_pix(2) >= 0.5  );
  outpix(3) = ( u_state_pix(3) >= 0.5  );

qerr=outpix-u_state_pix;
out(y,x,:)=outpix;

qerr = clip(qerr,-0.5,0.5);

num = num + (outpix-0.5)*(u_state_pix-0.5)';
den = den + (u_state_pix-0.5)*(u_state_pix-0.5)';
u_mean = u_mean + (u_state_pix-0.5);
out_mean = out_mean + (outpix-0.5);


qn(y+r0,x+c0,:)=qerr;

end

if (dir == -1)
   step = -step; temp = ep; ep = sp; sp=temp;
   f11=f11(:,cm+1:-1:1);f12=f12(:,cm+1:-1:1);f13=f13(:,cm+1:-1:1);
   f21=f21(:,cm+1:-1:1);f22=f22(:,cm+1:-1:1);f23=f23(:,cm+1:-1:1);
   f31=f31(:,cm+1:-1:1);f32=f32(:,cm+1:-1:1);f33=f33(:,cm+1:-1:1);   
end

if v

fprintf('\r Dithering,...% 3d %% done',round(y/ri * 100)),end
end

if v
 fprintf('\n');
end

qn = qn(r0+1:r0+ri,c0+1:c0+ci,:);

u = out - qn;

out_mean(1) = sum(sum(out(:,:,1)));
out_mean(2) = sum(sum(out(:,:,2)));
out_mean(3) = sum(sum(out(:,:,3)));

u_mean(1) = sum(sum(u(:,:,1)));
u_mean(2) = sum(sum(u(:,:,2)));
u_mean(3) = sum(sum(u(:,:,3)));



num=num-out_mean*u_mean';
den = den - u_mean*u_mean';

K=num*inv(den);

