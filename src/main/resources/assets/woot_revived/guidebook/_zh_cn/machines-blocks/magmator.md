---
navigation:
    parent: machines-blocks/machines-blocks-index.md
    title: "熔动机"
    icon: "woot_revived:netherite_magmator"
---
# 熔动机

<Row>
    <BlockImage id="copper_magmator" scale="2" />
    <BlockImage id="iron_magmator" scale="2" />
    <BlockImage id="gold_magmator" scale="2" />
    <BlockImage id="diamond_magmator" scale="2" />
    <BlockImage id="netherite_magmator" scale="2" />
</Row>

熔动机是<ItemImage id="stygian_anvil" scale="0.5"/>[幽冥砧](anvil.md)的自动化装置。

将其放置在幽冥砧下方后，它会每隔一定刻尝试完成合成，并将成品放入相邻的箱子中。
若其侧面没有可用的箱子，则不会执行任何操作。

你可以 Shift+右击该方块来切换其红石模式。

各等级熔动机的刻频如下：
- <ItemImage id="copper_magmator" scale="0.5"/>铜熔动机：<WootConfig key="magmator.copper_tick_rate" /> 刻/次合成
- <ItemImage id="iron_magmator" scale="0.5"/>铁熔动机：<WootConfig key="magmator.iron_tick_rate" /> 刻/次合成
- <ItemImage id="gold_magmator" scale="0.5"/>金熔动机：<WootConfig key="magmator.gold_tick_rate" /> 刻/次合成
- <ItemImage id="diamond_magmator" scale="0.5"/>钻石熔动机：<WootConfig key="magmator.diamond_tick_rate" /> 刻/次合成
- <ItemImage id="netherite_magmator" scale="0.5"/>下界合金熔动机：<WootConfig key="magmator.netherite_tick_rate" /> 刻/次合成

## 合成

<Row>
    <RecipeFor id="copper_magmator" />
    <RecipeFor id="iron_magmator" />
    <RecipeFor id="gold_magmator" />
    <RecipeFor id="diamond_magmator" />
    <RecipeFor id="netherite_magmator" />
</Row>
