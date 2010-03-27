const void* MRetainNoOp(CFAllocatorRef allocator, const void *value);
void MReleaseNoOp(CFAllocatorRef allocator, const void *value);

@interface NSMutableSet (SetNonRetaining)
+ (NSMutableSet*) setNonRetaining;
@end
