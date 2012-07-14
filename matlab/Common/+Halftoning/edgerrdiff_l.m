function [out,qn,L_adap] = edgerrdiff_l(in,fc,l,dir,v,dbf,nu)

% EDGERRDIFF_L Optimum Edge Enhancement Error Diffusion Halftoning by 
%  adapting sharpness parameter L
%  Description
%  [OUT,QN,L_ADAP] = edgerrdiff(IN,FC,L,DIR,V,DBF,Nu) performs edge enhanced 
%	error diffusion on image IN using error filter FC, modified parameter L
%	and direction DIR. l is raster scan and -l is serpentine scan.
%	When V (verbose) is non-zero, progress is printed to the output.
%   DBF sets the Deterministic Bit Flipping Quantizer Threshold [0,1] and Nu is the 
%   adaptation step size. 
%   The function returns the output image matrix, quantized image and L_ADAP -- the vector 
%   of sharpness parameter L. 
%   Input range is 0 to 1, as is output range.  Input image is modified
%	(no error pipe) for maximum speed.  
%
%	See also EDGERRDIFF_L_H, EDGERRFIXED, DISP_ERR.
%
%	Ref: 
%   1.) R. Eschbach and K. Knox, "Error diffusion algorithm with
%	edge enhancement", J. Opt. Soc. Am. A, Vol. 8, No. 12, December
%	1991, pp. 1844-1850.
%   2.) N. Damera-Venkata and Evans,"Adaptive Threshold Modulation for 
%   Error Diffusion Halftoning", IEEE Transactions on Image Processing, 
%   Vol. 10, No. 1, January 2001 

% Authored 2001, N. Damera-venkata, modified July 2002 Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.



if nargin<5					% default to verbose
   v=1;
   dbf = 0.2;
end
if nargin<4					% default to raster
  dir=1; end
if nargin<3					% default to unmodified
  l=0; end
if nargin<2					% default to Floyd-Steinberg
   fc = [1 5 3; 7 (-99*16) 0]/16; end

in = (in/255)*2-1;              %scale the input to correct range
%in = in/255;            % for [0,1]

[ri,ci]=size(in);
[rm,cm]=size(fc);
[r0,c0]=find(fc==-99);				% find origin of error filter
fc(r0,c0)=0;

rm=rm-1; cm=cm-1;
inpad=zeros(ri+rm,ci+cm);			% modified input image
inpad(r0:r0+ri-1,c0:c0+ci-1)=in;
out=zeros(ri,ci); qn=zeros(size(inpad));
%out=-1*ones(ri,ci);qn=-ones(size(inpad));

%nu=.005;

sp=1; ep=ci; step=1;
r0=r0-1;c0=c0-1;

for y=1:ri
  for x=sp:step:ep
    inpix=inpad(y+r0,x+c0); 

%if(y==64)
%  nu=.005;
%end

%if(y==384)
% nu=.0005;
%end

S=sum(sum(fc.*qn(y:y+rm,x:x+cm)));
u_state_pix = inpix - S;
 
 %if(u_state_pix + l*inpix >= .5) 
  %  outpix=1;
 %else 
  %  outpix=0;
 %end


 if(u_state_pix + l*inpix >= 0) 
    outpix=1;
 else 
    outpix=-1;
 end
 
 
 if(dbf ~= 0)
	if(abs(u_state_pix + l*inpix)<dbf)
 		outpix=-outpix;
	end
end



qerr=outpix-u_state_pix;
out(y,x)=outpix;

qn(y+r0,x+c0)=qerr;


tst=(inpix*(1+l)-S);

if( abs(tst) < 1 )
   s=1;
else
   s=0;
end

%if(abs(tst) <=.1)
% s=0;
%end

%if(abs(tst)>=.2)
% s=1;
%end

if(dbf~=0)
	if(tst >=.2 | (( tst <= 0) & (tst > -.2) ))
 		q=1;
	else 
 		q=-1;
    end
 end



%dl=2*inpix*(2*(inpix*(1+l)-S)-inpix); i am not sure what this does

if(dbf~=0)
   dl=2*s*inpix*(q-inpix); %Dithered Quantizer
else
   dl=(2*inpix*(sign(inpix*(1+l)-S)-inpix)); %Fixed quantizer
   %dl=2*inpix*(outpix-inpix); %Fixed quantizer [0,1]
end
if abs(inpix)>0.3
	nu=0.005;
else
	nu=0.05;
end
   
 l=l-nu*dl;



%Floyd adaptive
%dh = -2*qn(y:y+rm,x:x+cm)*(qerr-S);
%fc=fc-.05*dh;
%fc(2,2)=0;
%fc(2,3)=0;
%fc=fc + (1-sum(sum(fc)))/4;
%fc(2,2)=0;
%fc(2,3)=0;

%Jarvis adaptive
%dh = -2*qn(y:y+rm,x:x+cm)*(qerr-S);
%fc=fc-.005*dh;
%fc(3,3)=0;
%fc(3,4)=0;
%fc(3,5)=0;
%fc=fc + (1-sum(sum(fc)))/12;
%fc(3,3)=0;
%fc(3,4)=0;
%fc(3,5)=0;


%end

L(y,x)=l;

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

L=L';
L_adap=L(:);

qn = qn(r0:r0+ri-1,c0:c0+ci-1);





