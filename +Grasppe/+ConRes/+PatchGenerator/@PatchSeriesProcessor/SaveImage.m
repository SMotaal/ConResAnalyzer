function pth = SaveImage(img, type, id)
  import(eval(NS.CLASS)); % PatchSeriesProcessor

  try
    %if isstruct(id)
    if isempty(strfind(lower(type), 'image'))
      type = [strtrim(type) ' image'];
    end
    pth = PatchSeriesProcessor.GetResourcePath(type, id, 'png');
    imwrite(img, pth);
  catch err
    debugStamp(err, 1);
    return;
  end
end
