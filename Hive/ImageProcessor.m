//
//  ImageProcessor.m
//  Hive
//
//  Created by Denis Kubasov on 14/06/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "ImageProcessor.h"

@implementation ImageProcessor

- (void)processFrame:(CVPixelBufferRef)pixelBuffer {

   size_t w = CVPixelBufferGetWidth(pixelBuffer);
   size_t h = CVPixelBufferGetHeight(pixelBuffer);
   //printf("it's a pixel data! [%zu,%zu]\n", w, h);
   
   CVPixelBufferLockBaseAddress(pixelBuffer, kCVPixelBufferLock_ReadOnly);
   
   printf("Got new frame %zu x %zu with base adress %p\n", w, h, CVPixelBufferGetBaseAddress(pixelBuffer));
   
   CVPixelBufferUnlockBaseAddress(pixelBuffer, kCVPixelBufferLock_ReadOnly);

}

@end
