function img = LoadImage(type, id)
  import(eval(NS.CLASS));   % PatchSeriesProcessor
  
  img = [];
  if ~exist('id', 'var') %|| ~(exist(type, 'file')>0)
    try img = imread(type); end
  else
    if isempty(strfind(lower(type), 'image'))
      type = [strtrim(type) ' image'];
    end
    pth = PatchSeriesProcessor.GetResourcePath(type, id, 'png');
    try img = imread(pth); end
  end
end
