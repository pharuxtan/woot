---
navigation:
    parent: machines-blocks/machines-blocks-index.md
    title: "アイテム注入機"
    icon: "woot_revived:item_infuser"
---
# アイテム注入機

<BlockImage id="item_infuser" scale="5"/>

<ItemImage id="item_infuser" scale="0.5"/>アイテム注入機は材料を混ぜて液体を注入することでアイテムを生成します。

## クラフト

<RecipeFor id="item_infuser" />

## プリズム

<ItemImage id="prism" scale="0.5"/>プリズムは<ItemImage id="fake_spawner" scale="0.5"/>疑似スポナーを作るための主な材料です。

<Recipe id="item_infuser/prism" />

## 染料板

染料板を入手するには、対応する染料型を入手する必要があります。

以下は<ItemImage id="white_dye_casing" scale="0.5"/>白色の染料型の作成例ですが、各色の染料で同様に作成可能です。

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="../assets/anvil/dye_casing.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="white_dye_casing" scale="2"/>
  これを作るには、<ItemImage id="dye_casing_mold" scale="0.5"/>染料型の金型と、<ItemImage id="minecraft:white_dye" scale="0.5"/>白色の染料が必要です。
</Row>

染料型を入手したら、アイテム注入機で<ItemImage id="pure_dye_fluid_bucket" scale="0.5"/>純粋な染料液と組み合わせて染料板を作成できます。

<Recipe id="item_infuser/white_dye_plate" />

## エンチャントされた板金

バイタリティセルを作成するには、それぞれのエンチャントされた板金が必要です

例えば、<ItemImage id="copper_cell" scale="0.5"/>銅のバイタリティセルを入手するには、<ItemImage id="copper_enchanted_plate" scale="0.5"/>エンチャントされた銅板が必要になります。

これらの板金を作成するには、<ItemImage id="enchanted_fluid_bucket" scale="0.5"/>エンチャント液と各種の欠片も必要です。

<Row>
  <Recipe id="item_infuser/copper_enchanted_plate" />
  <Recipe id="item_infuser/iron_enchanted_plate" />
  <Recipe id="item_infuser/gold_enchanted_plate" />
  <Recipe id="item_infuser/diamond_enchanted_plate" />
  <Recipe id="item_infuser/netherite_enchanted_plate" />
</Row>

## その他

アイテム注入機を使ってバニラアイテムを作成することもできます。

<Row>
    <Recipe id="item_infuser/magma_block" />
    <Recipe id="item_infuser/netherrack" />
    <Recipe id="item_infuser/fire_charge" />
    <Recipe id="item_infuser/crying_obsidian" />
    <Recipe id="item_infuser/soul_soil" />
</Row>