---
navigation:
    parent: factory/factory-index.md
    title: "鉄"
    icon: "woot_revived:iron_cell"
    position: 2
---
# 鉄

ファクトリーの2つ目ティア。

## デザイン

プレビューで小さく表示される疑似スポナーはオプションであることに注意してください。

2つ目のティアに設定した<ItemImage id="layout" scale="0.5"/>[レイアウト](../machines-blocks/layout.md#iron)、は建設をするのに役に立ちます。

<GameScene zoom="2.5" interactive={true}>
    <ImportStructure src="../assets/factory/iron.snbt" />
    <IsometricCamera yaw="195" pitch="6" />
</GameScene>

## 鉄ティアのブロック

<Row>
  <BlockImage id="iron_pylon" scale="4" p:attached="true" />
  <BlockImage id="iron_plinth" scale="4" p:attached="true" />
  <BlockImage id="iron_cell" scale="4" p:attached="true" />
</Row>


これには、
<ItemImage id="iron_pylon" scale="0.5"/>鉄のパイロンが20個、
<ItemImage id="iron_plinth" scale="0.5"/>鉄の土台が12個、
<ItemImage id="iron_cell" scale="0.5"/>鉄のバイタリティセルが1個必要です。

<Row>
  <RecipeFor id="iron_pylon" />
  <RecipeFor id="iron_plinth" />
  <RecipeFor id="iron_cell" />
</Row>