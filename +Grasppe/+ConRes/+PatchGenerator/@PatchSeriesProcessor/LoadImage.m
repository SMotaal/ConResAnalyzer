function img = LoadImage(obj, type, id)
  if isempty(strfind(lower(type), 'image'))
    type = [strtrim(type) ' image'];
  end
  pth = obj.GetPath(type, id, 'png');
  try img = imread(pth); end
end
