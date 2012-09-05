function [pths imgs] = OutputImages(obj, src, type, id, retinalAccuity, imageOut)
  pths                    = cell(1, 2);
  imgs                    = cell(1, 2);
  
  retinaOut = exist('retinalAccuity', 'var') & isscalar(retinalAccuity) & isnumeric(retinalAccuity);
  imageOut  = exist('imageOut',  'var') | isequal(imageOut , false);
  
  if ~imageOut
    pths{1}               = {obj.GetPath([type ' image'], id, 'png')};
    if retinaOut, pths{2} = {obj.GetPath([type ' retinaImage'], id, 'png')}; end
    return;
  end
  
  if isnumeric(src)
    imgs{1}               = src;
  elseif isobject(src)
    imgs{1}               = src.Image;
  end
  
  pths{1}                 = obj.SaveImage(imgs{1}, type, id);
  
  if ~retinaOut, return; end
  
  disk                    = @(x, y)     imfilter(x,fspecial('disk',y),'replicate');
  
  imgs{2}                 = disk(imgs{1}, retinalAccuity);
  pths{2}                 = obj.SaveImage(imgs{2}, [type ' retinaImage'], id);
end
