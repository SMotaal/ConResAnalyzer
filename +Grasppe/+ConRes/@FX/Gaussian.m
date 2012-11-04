function [ fltimg ] = Gaussian( img, sigma )
  %GAUSSIAN Summary of this function goes here
  %   Detailed explanation goes here
  
	fltimg = imfilter(img,fspecial('gaussian',round(sigma*20), sigma),'replicate');  
  
end

