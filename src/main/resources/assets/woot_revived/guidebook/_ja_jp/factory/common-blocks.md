---
navigation:
    parent: factory/factory-index.md
    title: "汎用ブロック"
    icon: "woot_revived:factory_connect"
    position: 0
---
# 汎用ブロック

どのファクトリーもこれらのブロックを共通して使用します。配置の詳細については、各ティアのページをご覧ください。

## ファクトリーハート

<ItemImage id="heart" scale="0.5"/> [ファクトリーハート](../machines-blocks/heart.md)はファクトリーのメインコントローラーです。

<RecipeFor id="heart" />

## アップグレードスロット

<ItemImage id="factory_upgrade" scale="0.5"/> [アップグレードスロット](../machines-blocks/upgrade-slot.md)はファクトリーのアップグレードアイテムを保管します。

<RecipeFor id="factory_upgrade" />

## 疑似スポナー

<ItemImage id="fake_spawner" scale="0.5"/> [疑似スポナー](../mob-shard.md)はシミュレートするモブを保存します

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="../assets/anvil/fake_spawner.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="fake_spawner" scale="2"/>
  これを作るには<ItemImage id="mob_shard" scale="0.5"/>モブシャード、<ItemImage id="prism" scale="0.5"/>プリズム、<ItemImage id="factory_base" scale="0.5"/>ファクトリーベースが必要です
</Row>

## プライマリベースとセカンダリベース

<ItemImage id="factory_ctr_base_pri" scale="0.5"/>プライマリベースと<ItemImage id="factory_ctr_base_sec" scale="0.5"/>セカンダリベースは<ItemImage id="fake_spawner" scale="0.5"/>疑似スポナーの土台です。

セカンダリベースの疑似スポナーの設置はオプションであることに注意してください。

<Row>
  <RecipeFor id="factory_ctr_base_pri" />
  <RecipeFor id="factory_ctr_base_sec" />
</Row>

## ファクトリーコネクター

<ItemImage id="factory_ctr_base_pri" scale="0.5"/>ファクトリーコネクターは原材料をファクトリーに橋渡しします。

<RecipeFor id="factory_connect" />

## 原材料搬入機

<ItemImage id="import" scale="0.5"/> [原材料搬入機](../machines-blocks/import.md)は、モブを生成するために必要なアイテムや流体を搬入できます。

<RecipeFor id="import" />

## 戦利品搬出機
<ItemImage id="export" scale="0.5"/> [戦利品搬出機](../machines-blocks/export.md)は、生産したアイテムや液体を搬出します。

<RecipeFor id="export" />

## バイタリティセル

すべてのバイタリティセルは、全ファクトリーのティアで共通であることに注意してください！

[レイアウト](../machines-blocks/layout.md)を使用すると、バイタリティセルブロックが各バイタリティセルの種類に応じて切り替わるのがわかります。