---
navigation:
    parent: factory/factory-index.md
    title: "金"
    icon: "woot_revived:gold_cell"
    position: 3
---
# 金

ファクトリーの3つ目ティア。

## デザイン

プレビューで小さく表示される疑似スポナーはオプションであることに注意してください。

3つ目のティアに設定した<ItemImage id="layout" scale="0.5"/>[レイアウト](../machines-blocks/layout.md#gold)、は建設をするのに役に立ちます。

<GameScene zoom="2.5" interactive={true}>
    <ImportStructure src="../assets/factory/gold.snbt" />
    <IsometricCamera yaw="195" pitch="6" />
</GameScene>

## 金ティアのブロック

<Row>
  <BlockImage id="gold_pylon" scale="4" p:attached="true" />
  <BlockImage id="gold_plinth" scale="4" p:attached="true" />
  <BlockImage id="gold_cell" scale="4" p:attached="true" />
</Row>


これには、
<ItemImage id="gold_pylon" scale="0.5"/>金のパイロンが36個、
<ItemImage id="gold_plinth" scale="0.5"/>金の土台が24個、
<ItemImage id="gold_cell" scale="0.5"/>金のバイタリティセルが1個必要です。

<Row>
  <RecipeFor id="gold_pylon" />
  <RecipeFor id="gold_plinth" />
  <RecipeFor id="gold_cell" />
</Row>