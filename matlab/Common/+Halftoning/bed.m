
%BED Function to perform block error diffusion
%[result] = bed(pD,N,mask1,mask0,l)
%pD->image to be halftoned[0,255]
%N->block size ex: [1 2]
%mask1-> mask for ones ex: [1 1]
%mask0-> maskfor zeros ex: [0 0] (typically this is 1-mask1)
%l->sharpness parameter 
%
%Examples:
%  u=bed(o,[1 2],[1 1],[0 0],0); 1x2 blocks with rectangular minority pixels
%  u=bed(o,[3 3],[0 1 0;1 1 1;0 1 0],[1 0 1;0 0 0;1 0 1],0); 3x3 blocks with "plus" minority pixels
%
% Ref: Niranjan-Damera Venkata, Vishal Monga and Brian L. Evans ''Clustered dot FM halftoning via Block 
% Error Diffusion", Submitted to IEEE Transactions on Image Processing, August 7, 2002

% Authored 2001, Niranjan Damera-Venkata, modified July 2002
% by Vishal Monga

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function [result] = bed(pD,N,mask1,mask0,l)

N1 = N(1); % Extracting Block dimensions
N2 = N(2); % ----------do --------------

fback = zeros(N1,N2); %Creating a matrix of zeros of size N1 x N2
MN = N1*N2;           %Number of pixels in the BLOCK

thresh = 128*ones(N1,N2); %Thresholding matrix -- threshold set to 128 (i.e 0.5 for graylevels in [0,1])

[ri ci] = size(pD);  % Extracting i/p image size
rim = ri - mod(ri,N1); %modified size when image size is not a multiple of block size
cim = ci - mod(ci,N2); %---------------------------do--------------------------------

error = zeros(1,ceil(ci/N2));
iprime = 1;
jprime = 1;

D = 1/(MN); %Value of each element in the diffusion matrix

out = zeros(rim,cim);  %Start with an o/p halftone of modified size with all zeros!
corn_pix = 0;          %Set corner pixel to zero
ctr =  0;

for i = 1:N1:rim
  
   for j = 1:N2:cim
         
         if(i~=1)
           iprime = ((i-1)/N1)+1;
         else
           iprime = 1;
         end %end of if1
         
         if(j~=1)
           jprime = ((j-1)/N2)+1;
         else
           jprime = 1;
         end %end of if2
        
         fback_scal = 0;              %Feedback Error for FS error diffusion
         if(jprime > 1)
           fback_scal = fback_scal + (7/16)*error(1,jprime-1);
      	 if(iprime > 1)
         	fback_scal = fback_scal + (1/16)*corn_pix;
      	 end %end of if3
         end %end of if4
      
         if((iprime > 1))
            fback_scal = fback_scal + (5/16)*error(1,jprime);
         if((jprime+1) < ceil(ci/N2))
            fback_scal = fback_scal + (3/16)*error(1,jprime+1);
         end  
         end
      
      fback = fback_scal*ones(N1,N2);
      inpix = pD(i:i+N1-1,j:j+N2-1);
      mod_block = clip(inpix - fback,0,255);
      boost_mod_block = l*inpix + mod_block;

      %%Determine if current pixel is a minority pixel%%
      minor = ~(sum(sum((inpix >= thresh)))>=(MN/2));
      outmap = (boost_mod_block >= thresh);
      min_flag = ((sum(sum(outmap))>=(MN/2)) == minor);
           
     if(min_flag == 1)
         if(minor == 1)
            outpix = (mask1);
         else
            outpix = (mask0);
         end
     else
         outpix = outmap;
         %major = ~minor;
         %outpix = (major)*ones(N1,N2);
     end
     
      out(i:i+N1-1,j:j+N2-1) = outpix;
      corn_pix = error(1,jprime);
      error(1,jprime) = mean(mean(255*outpix - mod_block));
      
   end
%   fprintf('\r Dithering,...% 3d %% done',round(i/ri * 100))
end

sizeout = size(out);
less = min([sizeout(1) sizeout(2)]);
result = out(1:less,1:less);
result(less:ri,less:ci) = 0;
return



      
      
      
      
      
      
         
      
         
         



