---
navigation:
    parent: machines-blocks/machines-blocks-index.md
    title: "マグマーター"
    icon: "woot_revived:netherite_magmator"
---
# マグマーター

<Row>
    <BlockImage id="copper_magmator" scale="2" />
    <BlockImage id="iron_magmator" scale="2" />
    <BlockImage id="gold_magmator" scale="2" />
    <BlockImage id="diamond_magmator" scale="2" />
    <BlockImage id="netherite_magmator" scale="2" />
</Row>

マグマーターは<ItemImage id="stygian_anvil" scale="0.5"/>[スティジアンの金床](anvil.md)を自動化するためのブロックです。

金床の下に設置すると、Xティックごとにアイテムのクラフトを試み、隣接するチェストに搬入します。
ブロックの横にチェストが存在しない場合、動作しません。

また、ブロックをShiftキーを押しながらクリックすると、レッドストーンモードを変更できます。

各マグマーターのティックあたりのクラフト回数は以下の通りです：
- <ItemImage id="copper_magmator" scale="0.5"/> 銅のマグマーター: <WootConfig key="magmator.copper_tick_rate" /> ticks/クラフト
- <ItemImage id="iron_magmator" scale="0.5"/> 鉄のマグマーター: <WootConfig key="magmator.iron_tick_rate" /> ticks/クラフト
- <ItemImage id="gold_magmator" scale="0.5"/> 金のマグマーター: <WootConfig key="magmator.gold_tick_rate" /> ticks/クラフト
- <ItemImage id="diamond_magmator" scale="0.5"/> ダイヤモンドのマグマーター: <WootConfig key="magmator.diamond_tick_rate" /> ticks/クラフト
- <ItemImage id="netherite_magmator" scale="0.5"/> ネザライトのマグマーター: <WootConfig key="magmator.netherite_tick_rate" /> ticks/クラフト

## Craft

<Row>
    <RecipeFor id="copper_magmator" />
    <RecipeFor id="iron_magmator" />
    <RecipeFor id="gold_magmator" />
    <RecipeFor id="diamond_magmator" />
    <RecipeFor id="netherite_magmator" />
</Row>