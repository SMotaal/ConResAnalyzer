%color_model.m
%script to validate model by linearly distorting an original

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function [out] = color_model(in,F,l,K_v,p,v)

in(:,:,1)=(in(:,:,1)/255)*2 - 1;
in(:,:,2)=(in(:,:,2)/255)*2 - 1;
in(:,:,3)=(in(:,:,3)/255)*2 - 1;

if nargin<5					% default to verbose
  v=1; end
if nargin<4					% default to raster
  dir=1; end
if nargin<3					% default to unmodified
  l=[0 0 0;0 0 0;0 0 0]; end
if nargin<2					% default to Floyd-Steinberg

f1=[7 0 0;0 7 0;0 0 7]/16;
f2=[1 0 0;0 1 0;0 0 1]/16;
f3=[5 0 0;0 5 0;0 0 5]/16;
f4=[3 0 0;0 3 0;0 0 3]/16;


F=[f1 f2 f3 f4];
end



f1=F(:,1:3);
f2=F(:,4:6);
f3=F(1:3,7:9);
f4=F(:,10:12);


[ri,ci]=size(in(:,:,1));
rm=2;
cm=3;
r0=2;
c0=2;


rm=rm-1; cm=cm-1;
%inpad=zeros(ri+rm,ci+cm,3);			% modified input image

inpad=-ones(ri+rm,ci+cm,3);

inpad(r0:r0+ri-1,c0:c0+ci-1,1)=in(:,:,1);
inpad(r0:r0+ri-1,c0:c0+ci-1,2)=in(:,:,2);
inpad(r0:r0+ri-1,c0:c0+ci-1,3)=in(:,:,3);



out=-ones(ri,ci,3);qn=-ones(size(inpad));


sp=1; ep=ci; step=1;
r0=r0-1;c0=c0-1;

F=[f1 f2 f3 f4];

for y=1:ri
  for x=sp:step:ep
    inpix=array2ve(inpad(y+r0,x+c0,:)); 



S=f2*array2ve(qn(y,x,:)) + f3*array2ve(qn(y,x+1,:)) + f4*array2ve(qn(y,x+2,:)) + f1*array2ve(qn(y+1,x,:));

u_state_pix = inpix - S;
 
%%% QUANTIZATION STEP
%outpix=quantize( (u_state_pix + l*inpix),qtype,p); % 1-Scalar quantization 2-VQ    

%test model
outpix = K_v*(u_state_pix + l*inpix);

qerr=outpix-u_state_pix;

out(y,x,1)=outpix(1);
out(y,x,2)=outpix(2);
out(y,x,3)=outpix(3);

qn(y+r0,x+c0,1)=qerr(1);
qn(y+r0,x+c0,2)=qerr(2);
qn(y+r0,x+c0,3)=qerr(3);

end

if v

fprintf('\r Dithering,...% 3d %% done',round(y/ri * 100))

end


end

if v
 fprintf('\n');
end


%qnx(:,:,1)=qn(r0:r0+ri-1,c0:c0+ci-1,1);
%qnx(:,:,2)=qn(r0:r0+ri-1,c0:c0+ci-1,2);
%qnx(:,:,3)=qn(r0:r0+ri-1,c0:c0+ci-1,3);

%sig_inp_q(:,:,1)=out(:,:,1)-qnx(:,:,1);
%sig_inp_q(:,:,2)=out(:,:,2)-qnx(:,:,2);
%sig_inp_q(:,:,3)=out(:,:,3)-qnx(:,:,3);



