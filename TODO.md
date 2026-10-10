AI gen:
---

- [ ] Replace the `ItemStackHandler` field in `StorageBlockEntity` with a `Storage` instance
- [ ] Remove the `getInventory()` method that returned `ItemStackHandler`
- [ ] Update `saveAdditional` to call `storage.save(registries)` and store the result
- [ ] Update `loadAdditional` to call `storage.load(tag, registries)`
- [ ] Add a `getStorage()` method that returns the `Storage` instance (needed by the menu later)

---

- [ ] Create `StorageItemHandler` class in the storage package that implements `IItemHandler`
- [ ] Give it a reference to a `Storage` instance in its constructor
- [ ] Implement `getSlots()` — decide how many virtual slots to expose (e.g. one per item type stored)
- [ ] Implement `getStackInSlot(int slot)` — map each slot index to a stored item type and return an `ItemStack` with the stored count (clamped to `Integer.MAX_VALUE`)
- [ ] Implement `insertItem(int slot, ItemStack stack, boolean simulate)` — delegate to `storage.insert()`, respect the `simulate` flag
- [ ] Implement `extractItem(int slot, int amount, boolean simulate)` — delegate to `storage.extract()`, respect the `simulate` flag
- [ ] Implement `getSlotLimit(int slot)` — return `Integer.MAX_VALUE`
- [ ] Implement `isItemValid(int slot, ItemStack stack)` — return `true`
- [ ] Register the handler as a NeoForge capability on the block entity using `registerCapabilities` in `StorageBlockEntity`
- [ ] Update `StorageMenu` to accept `StorageItemHandler` instead of raw `IItemHandler`

---

- [ ] Create a `StorageContentsPayload` record implementing `CustomPacketPayload` — carries the full `Map<ItemKey<Item>, Long>` as a list
- [ ] Register the payload type in your mod setup
- [ ] In `StorageMenu` server-side constructor, send the payload to the player when the menu opens
- [ ] Create a client-side handler that receives the payload and updates the menu's local copy of the storage map
- [ ] Add a `Map<ItemKey<Item>, Long>` field to `StorageMenu` to hold the client-side view of the storage
- [ ] Add a getter on `StorageMenu` for that map so `StorageScreen` can read it
- [ ] Update `StorageScreen.renderBg()` to iterate the map and draw each item type with its count

---