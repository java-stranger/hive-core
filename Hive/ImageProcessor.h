//
//  ImageProcessor.h
//  Hive
//
//  Created by Denis Kubasov on 14/06/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#ifndef Hive_ImageProcessor_h
#define Hive_ImageProcessor_h

@import AVFoundation;
#include "CameraCapture.h"

@interface ImageProcessor : NSObject <ProcessFrameDelegate>

- (void)processFrame:(CVPixelBufferRef)pixelBuffer;

@end

#endif
