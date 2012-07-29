function setParentPosition( jComponent, position )
  %SETPARENTPOSITION Summary of this function goes here
  %   Detailed explanation goes here
  
  if numel(position)==1 && ishandle(position)
    position = pixelPosition(position);
    try position = pixelPosition(position, true); end
  end
  
  try
    x = position(1);
    y = position(2);
    w = position(3);
    h = position(4);
    disp([x y w h]);
    %jComponent.getParent.setPosition(x,y,w,h);
    jComponent.getParent.setSize(w,h); jComponent.getParent.setLocation(x,y);
  end
  
end

