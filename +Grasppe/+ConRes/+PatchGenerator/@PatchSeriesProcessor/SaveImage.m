function pth = SaveImage(obj, img, type, id)
  try
    %if isstruct(id)
    if isempty(strfind(lower(type), 'image'))
      type = [strtrim(type) ' image'];
    end
    pth = obj.GetPath(type, id, 'png');
    imwrite(img, pth);
  catch err
    debugStamp(err, 1);
  end
end
