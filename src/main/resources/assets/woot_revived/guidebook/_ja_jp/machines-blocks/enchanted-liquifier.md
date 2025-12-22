---
navigation:
    parent: machines-blocks/machines-blocks-index.md
    title: "エンチャント液化機"
    icon: "woot_revived:enchanted_liquifier"
---
# エンチャント液化機

<BlockImage id="enchanted_liquifier" scale="5"/>

<ItemImage id="enchanted_liquifier" scale="0.5"/>エンチャント液化機はエンチャントの本を液化することで<ItemImage id="enchanted_fluid_bucket" scale="0.5"/>エンチャント液を生成します。

<ItemImage id="minecraft:enchanted_book" scale="0.5"/>エンチャントの本のレベルごとに<WootConfig key="enchanted_liquifier.per_enchant_fluid" />mBの<ItemImage id="enchanted_fluid_bucket" scale="0.5"/>エンチャント液が生成されます。上限は<WootConfig key="enchanted_liquifier.max_enchant_lvl" />までです。

つまり、本のエンチャントレベルが<WootConfig key="enchanted_liquifier.max_enchant_lvl" />を超える場合、<WootConfig key="enchanted_liquifier.max_enchant_lvl" />レベル相当として扱われます。

<EnchantedRecipe />

## クラフト


<RecipeFor id="enchanted_liquifier" />
