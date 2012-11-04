function [ fltimg ] = Retina( img, accuity, factor )
  %RETINA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.FX;
  
  if ~exist('factor', 'var'), factor = 3; end
  
  fltimg    = img;
  
  for m = 1:factor
    fltimg  = FX.Gaussian(fltimg, accuity);    
  end
  
  %fltimg = FX.Gaussian(img, accuity.*factor);
  
end

