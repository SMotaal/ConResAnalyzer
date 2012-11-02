function img = LoadImage(type, id)
  % import(eval(NS.CLASS));   % PatchSeriesProcessor
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  img = [];
  if ~exist('id', 'var') %|| ~(exist(type, 'file')>0)
    try
      img = imread(type);
    catch err
      debugstamp;
    end
  else
    if isempty(strfind(lower(type), 'image'))
      type = [strtrim(type) ' image'];
    end
    pth = PatchSeriesProcessor.GetResourcePath(type, id, 'png');
    try 
      img = imread(pth);
    catch err
      debugstamp;
    end
  end
end
