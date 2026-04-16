---
navigation:
    parent: machines-blocks/machines-blocks-index.md
    title: "マグメイター"
    icon: "woot_revived:netherite_magmator"
---
# マグメイター

<Row>
    <BlockImage id="copper_magmator" scale="2" />
    <BlockImage id="iron_magmator" scale="2" />
    <BlockImage id="gold_magmator" scale="2" />
    <BlockImage id="diamond_magmator" scale="2" />
    <BlockImage id="netherite_magmator" scale="2" />
</Row>

マグメイターは<ItemImage id="stygian_anvil" scale="0.5"/>[スティジアンの金床](anvil.md)を自動化するためのブロックです。

金床の下に設置すると、Xティックごとにアイテムのクラフトを試み、隣接するチェストに搬入します。
ブロックの横にチェストが存在しない場合、動作しません。

また、ブロックをShiftキーを押しながらクリックすると、レッドストーンモードを変更できます。

各マグメイターのティックあたりのクラフト回数は以下の通りです：
- <ItemImage id="copper_magmator" scale="0.5"/> 銅のマグメイター: <WootConfig key="magmator.copper_tick_rate" /> tick/クラフト
- <ItemImage id="iron_magmator" scale="0.5"/> 鉄のマグメイター: <WootConfig key="magmator.iron_tick_rate" /> tick/クラフト
- <ItemImage id="gold_magmator" scale="0.5"/> 金のマグメイター: <WootConfig key="magmator.gold_tick_rate" /> tick/クラフト
- <ItemImage id="diamond_magmator" scale="0.5"/> ダイヤモンドのマグメイター: <WootConfig key="magmator.diamond_tick_rate" /> tick/クラフト
- <ItemImage id="netherite_magmator" scale="0.5"/> ネザライトのマグメイター: <WootConfig key="magmator.netherite_tick_rate" /> tick/クラフト

## クラフト

<Row>
    <RecipeFor id="copper_magmator" />
    <RecipeFor id="iron_magmator" />
    <RecipeFor id="gold_magmator" />
    <RecipeFor id="diamond_magmator" />
    <RecipeFor id="netherite_magmator" />
</Row>