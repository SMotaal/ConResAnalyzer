function pth = SaveImage(img, type, id)
  import(eval(NS.CLASS)); % PatchSeriesProcessor

  try
    %if isstruct(id)
    if isempty(strfind(lower(type), 'image')) && isempty(strfind(lower(type), 'plot'))
      type = [strtrim(type) ' image'];
    end
    pth = PatchSeriesProcessor.GetResourcePath(type, id, 'png');
    imwrite(img, pth);
    
    Grasppe.ConRes.File.UpdateTimeStamp(pth);
  catch err
    debugStamp(err, 1);
    return;
  end
end
