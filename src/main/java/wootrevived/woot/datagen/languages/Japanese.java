package wootrevived.woot.datagen.languages;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.*;

public class Japanese extends LanguageProvider {
    public Japanese(PackOutput output){
        super(output, Woot.MOD_ID, "ja_jp");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.woot_revived", "Woot Revived");
        add("config.jade.plugin_woot_revived.machines", "Woot機械プラグイン");

        addBlock(BlocksRegistry.STYGIAN_ANVIL_BLOCK.get(), "スティジアンの金床");
        add(ItemsRegistry.STYGIAN_HAMMER_ITEM.get(), "スティジアンのハンマー");
        add(ItemsRegistry.PLATE_MOLD_ITEM.get(), "板金の金型");
        add(ItemsRegistry.SHARD_MOLD_ITEM.get(), "欠片の金型");
        add(ItemsRegistry.DYE_CASING_MOLD_ITEM.get(), "染料板の型の金型");

        addBlock(BlocksRegistry.CREATIVE_POWER_BLOCK.get(), "クリエイティブ電源");
        addBlock(BlocksRegistry.CREATIVE_TANK_BLOCK.get(), "クリエイティブタンク");

        addBlock(BlocksRegistry.FACTORY_BASE_BLOCK.get(), "ファクトリーベース");

        addBlock(BlocksRegistry.COPPER_MAGMATOR_BLOCK.get(), "銅のマグマーター");
        addBlock(BlocksRegistry.IRON_MAGMATOR_BLOCK.get(), "鉄のマグマーター");
        addBlock(BlocksRegistry.GOLD_MAGMATOR_BLOCK.get(), "金のマグマーター");
        addBlock(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK.get(), "ダイヤモンドのマグマーター");
        addBlock(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK.get(), "ネザライトのマグマーター");

        addBlock(BlocksRegistry.HEART_BLOCK.get(), "ファクトリーハート");
        addBlock(BlocksRegistry.FAKE_SPAWNER_BLOCK.get(), "疑似スポナー");

        addBlock(BlocksRegistry.COPPER_CELL_BLOCK.get(), "銅のバイタリティセル");
        addBlock(BlocksRegistry.COPPER_PYLON_BLOCK.get(), "銅のパイロン");
        addBlock(BlocksRegistry.COPPER_PLINTH_BLOCK.get(), "銅の土台");
        add(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get(), "エンチャントされた銅板");
        add(ItemsRegistry.COPPER_SHARD_ITEM.get(), "銅の欠片");

        addBlock(BlocksRegistry.IRON_CELL_BLOCK.get(), "鉄のバイタリティセル");
        addBlock(BlocksRegistry.IRON_PYLON_BLOCK.get(), "鉄のパイロン");
        addBlock(BlocksRegistry.IRON_PLINTH_BLOCK.get(), "鉄の土台");
        add(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get(), "エンチャントされた鉄板");
        add(ItemsRegistry.IRON_SHARD_ITEM.get(), "鉄の欠片");

        addBlock(BlocksRegistry.GOLD_CELL_BLOCK.get(), "金のバイタリティセル");
        addBlock(BlocksRegistry.GOLD_PYLON_BLOCK.get(), "金のパイロン");
        addBlock(BlocksRegistry.GOLD_PLINTH_BLOCK.get(), "金の土台");
        add(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get(), "エンチャントされた金板");
        add(ItemsRegistry.GOLD_SHARD_ITEM.get(), "金の欠片");

        addBlock(BlocksRegistry.DIAMOND_CELL_BLOCK.get(), "ダイヤモンドのバイタリティセル");
        addBlock(BlocksRegistry.DIAMOND_PYLON_BLOCK.get(), "ダイヤモンドのパイロン");
        addBlock(BlocksRegistry.DIAMOND_PLINTH_BLOCK.get(), "ダイヤモンドの土台");
        add(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get(), "エンチャントされたダイヤモンドの板");
        add(ItemsRegistry.DIAMOND_SHARD_ITEM.get(), "ダイヤモンドの欠片");

        addBlock(BlocksRegistry.NETHERITE_CELL_BLOCK.get(), "ネザライトのバイタリティセル");
        addBlock(BlocksRegistry.NETHERITE_PYLON_BLOCK.get(), "ネザライトのパイロン");
        addBlock(BlocksRegistry.NETHERITE_PLINTH_BLOCK.get(), "ネザライトの土台");
        add(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get(), "エンチャントされたネザライトの板");
        add(ItemsRegistry.NETHERITE_SHARD_ITEM.get(), "ネザライトの欠片");

        addBlock(BlocksRegistry.FACTORY_CONNECT_BLOCK.get(), "ファクトリーコネクター");
        addBlock(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK.get(), "プライマリベース");
        addBlock(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK.get(), "セカンダリベース");

        addBlock(BlocksRegistry.IMPORT_BLOCK.get(), "原材料搬入機");
        addBlock(BlocksRegistry.EXPORT_BLOCK.get(), "戦利品搬出機");

        addBlock(BlocksRegistry.FACTORY_UPGRADE_BLOCK.get(), "アップグレードスロット");

        add(Efficiency.COPPER_EFFICIENCY_ITEM.get(), "効率化アップグレード I");
        add(Efficiency.IRON_EFFICIENCY_ITEM.get(), "効率化アップグレード II");
        add(Efficiency.GOLD_EFFICIENCY_ITEM.get(), "効率化アップグレード III");
        add(Efficiency.DIAMOND_EFFICIENCY_ITEM.get(), "効率化アップグレード IV");
        add(Efficiency.NETHERITE_EFFICIENCY_ITEM.get(), "効率化アップグレード V");
        add("info.woot_revived.upgrade.efficiency.desc.0", "バイタリティ燃料液の消費量を%d%%軽減");

        add(Looting.COPPER_LOOTING_ITEM.get(), "ドロップ増加アップグレード I");
        add(Looting.IRON_LOOTING_ITEM.get(), "ドロップ増加アップグレード II");
        add(Looting.GOLD_LOOTING_ITEM.get(), "ドロップ増加アップグレード III");
        add(Looting.DIAMOND_LOOTING_ITEM.get(), "ドロップ増加アップグレード IV");
        add(Looting.NETHERITE_LOOTING_ITEM.get(), "ドロップ増加アップグレード V");
        add("info.woot_revived.upgrade.looting.desc.0", "ドロップ増加%dの効果を適用");

        add(Mass.COPPER_MASS_ITEM.get(), "並列化アップグレード I");
        add(Mass.IRON_MASS_ITEM.get(), "並列化アップグレード II");
        add(Mass.GOLD_MASS_ITEM.get(), "並列化アップグレード III");
        add(Mass.DIAMOND_MASS_ITEM.get(), "並列化アップグレード IV");
        add(Mass.NETHERITE_MASS_ITEM.get(), "並列化アップグレード V");
        add("info.woot_revived.upgrade.mass.desc.0", "%d体のモブを倒す");

        add(Rate.COPPER_RATE_ITEM.get(), "スピードアップグレード I");
        add(Rate.IRON_RATE_ITEM.get(), "スピードアップグレード II");
        add(Rate.GOLD_RATE_ITEM.get(), "スピードアップグレード III");
        add(Rate.DIAMOND_RATE_ITEM.get(), "スピードアップグレード IV");
        add(Rate.NETHERITE_RATE_ITEM.get(), "スピードアップグレード V");
        add("info.woot_revived.upgrade.rate.desc.0", "シミュレーションの所要時間を%d%%短縮");

        add(Decapitate.COPPER_DECAPITATE_ITEM.get(), "斬首アップグレード I");
        add(Decapitate.IRON_DECAPITATE_ITEM.get(), "斬首アップグレード II");
        add(Decapitate.GOLD_DECAPITATE_ITEM.get(), "斬首アップグレード III");
        add(Decapitate.DIAMOND_DECAPITATE_ITEM.get(), "斬首アップグレード IV");
        add(Decapitate.NETHERITE_DECAPITATE_ITEM.get(), "斬首アップグレード V");
        add("info.woot_revived.upgrade.decapitate.desc.0", "%dxモブの頭ドロップする確率");

        add(Xp.COPPER_XP_ITEM.get(), "経験値アップグレード I");
        add(Xp.IRON_XP_ITEM.get(), "経験値アップグレード II");
        add(Xp.GOLD_XP_ITEM.get(), "経験値アップグレード III");
        add(Xp.DIAMOND_XP_ITEM.get(), "経験値アップグレード IV");
        add(Xp.NETHERITE_XP_ITEM.get(), "経験値アップグレード V");
        add("info.woot_revived.upgrade.xp.desc.0", "%d%%の経験値が欠片としてドロップします");

        add(ShardDrop.IRON_SHARD_DROP_ITEM.get(), "欠片アップグレード I");
        add(ShardDrop.GOLD_SHARD_DROP_ITEM.get(), "欠片アップグレード II");
        add(ShardDrop.DIAMOND_SHARD_DROP_ITEM.get(), "欠片アップグレード III");
        add(ShardDrop.NETHERITE_SHARD_DROP_ITEM.get(), "欠片アップグレード IV");
        add("info.woot_revived.upgrade.shard_drop.desc.0", "%sの欠片とそれより下位の欠片をドロップします");
        add("info.woot_revived.upgrade.shard_drop.desc.1", "%s以上のティアが必要です");
        add("info.woot_revived.upgrade.shard_drop.desc.2", "欠片ドロップ率:%d%%");

        add(Burn.BURN_ITEM.get(), "火属性アップグレード");
        add("info.woot_revived.upgrade.burn.desc.0", "シミュレートされたモブを倒した際に炎を付与する");

        add(Dimension.NETHER_DIMENSION_ITEM.get(), "ネザーディメンションアップグレード");
        add(Dimension.END_DIMENSION_ITEM.get(), "エンドディメンションアップグレード");
        add("info.woot_revived.upgrade.dimension.desc.nether", "シミュレーションのディメンションをネザーに設定します");
        add("info.woot_revived.upgrade.dimension.desc.end", "シミュレーションのディメンションをエンドに設定します");

        add(ItemsRegistry.MOB_SHARD_ITEM.get(), "モブシャード");
        add(ItemsRegistry.MOB_SHARD_PROJECTILE.get(), "モブシャード");

        add(ItemsRegistry.XP_SHARD_ITEM.get(), "経験値の欠片");
        add(ItemsRegistry.XP_SPLINTER_ITEM.get(), "経験値の細片");

        addBlock(BlocksRegistry.FLUID_INFUSER_BLOCK.get(), "液体注入機");

        add(ItemsRegistry.STYGIAN_INGOT_ITEM.get(), "スティジアンインゴット");
        add(ItemsRegistry.STYGIAN_DUST_ITEM.get(), "スティジアンの粉");
        add(ItemsRegistry.STYGIAN_PLATE_ITEM.get(), "スティジアンの板");
        addBlock(BlocksRegistry.STYGIAN_BLOCK.get(), "スティジアンブロック");
        add(ItemsRegistry.PRISM_ITEM.get(), "プリズム");
        add(UpgradeItemsRegistry.UPGRADE_BASE_ITEM.get(), "アップグレードベース");

        addBlock(BlocksRegistry.ITEM_INFUSER_BLOCK.get(), "アイテム注入機");
        add(ItemsRegistry.WHITE_DYE_CASING_ITEM.get(), "白色の染料板の型");
        add(ItemsRegistry.WHITE_DYE_PLATE_ITEM.get(), "白色の染料板");
        add(ItemsRegistry.ORANGE_DYE_CASING_ITEM.get(), "橙色の染料板の型");
        add(ItemsRegistry.ORANGE_DYE_PLATE_ITEM.get(), "橙色の染料板");
        add(ItemsRegistry.MAGENTA_DYE_CASING_ITEM.get(), "赤紫色の染料板の型");
        add(ItemsRegistry.MAGENTA_DYE_PLATE_ITEM.get(), "赤紫色の染料板");
        add(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM.get(), "空色の染料板の型");
        add(ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM.get(), "空色の染料板");
        add(ItemsRegistry.YELLOW_DYE_CASING_ITEM.get(), "黄色の染料板の型");
        add(ItemsRegistry.YELLOW_DYE_PLATE_ITEM.get(), "黄色の染料板");
        add(ItemsRegistry.LIME_DYE_CASING_ITEM.get(), "黄緑色の染料板の型");
        add(ItemsRegistry.LIME_DYE_PLATE_ITEM.get(), "黄緑色の染料板");
        add(ItemsRegistry.PINK_DYE_CASING_ITEM.get(), "桃色の染料板の型");
        add(ItemsRegistry.PINK_DYE_PLATE_ITEM.get(), "桃色の染料板");
        add(ItemsRegistry.GRAY_DYE_CASING_ITEM.get(), "灰色の染料板の型");
        add(ItemsRegistry.GRAY_DYE_PLATE_ITEM.get(), "灰色の染料板");
        add(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM.get(), "薄灰色の染料板の型");
        add(ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM.get(), "薄灰色の染料板");
        add(ItemsRegistry.CYAN_DYE_CASING_ITEM.get(), "青緑色の染料板の型");
        add(ItemsRegistry.CYAN_DYE_PLATE_ITEM.get(), "青緑色の染料板");
        add(ItemsRegistry.PURPLE_DYE_CASING_ITEM.get(), "紫色の染料板の型");
        add(ItemsRegistry.PURPLE_DYE_PLATE_ITEM.get(), "紫色の染料板");
        add(ItemsRegistry.BLUE_DYE_CASING_ITEM.get(), "青色の染料板の型");
        add(ItemsRegistry.BLUE_DYE_PLATE_ITEM.get(), "青色の染料板");
        add(ItemsRegistry.BROWN_DYE_CASING_ITEM.get(), "茶色の染料板の型");
        add(ItemsRegistry.BROWN_DYE_PLATE_ITEM.get(), "茶色の染料板");
        add(ItemsRegistry.GREEN_DYE_CASING_ITEM.get(), "緑色の染料板の型");
        add(ItemsRegistry.GREEN_DYE_PLATE_ITEM.get(), "緑色の染料板");
        add(ItemsRegistry.RED_DYE_CASING_ITEM.get(), "赤色の染料板の型");
        add(ItemsRegistry.RED_DYE_PLATE_ITEM.get(), "赤色の染料板");
        add(ItemsRegistry.BLACK_DYE_CASING_ITEM.get(), "黒色の染料板の型");
        add(ItemsRegistry.BLACK_DYE_PLATE_ITEM.get(), "黒色の染料板");

        add(ItemsRegistry.GUIDE_BOOK_ITEM.get(), "Wootガイドブック");

        addBlock(BlocksRegistry.LAYOUT_BLOCK.get(), "ファクトリーレイアウト");

        addBlock(BlocksRegistry.DYE_LIQUIFIER_BLOCK.get(), "染料液化機");
        addBlock(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK.get(), "エンチャント液化機");

        add(FluidsRegistry.VITALITY_FUEL_FLUID_BLOCK.get(), "バイタリティ燃料液");
        add(FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET.get(), "バイタリティ燃料液入りバケツ");
        add("fluid_type.woot_revived." + FluidsRegistry.VITALITY_FUEL_FLUID_TAG, "バイタリティ燃料液");

        add(FluidsRegistry.ENCHANTED_FLUID_BLOCK.get(), "エンチャント液");
        add(FluidsRegistry.ENCHANTED_FLUID_BUCKET.get(), "エンチャント液入りバケツ");
        add("fluid_type.woot_revived." + FluidsRegistry.ENCHANTED_FLUID_TAG, "エンチャント液");

        add(FluidsRegistry.MOB_TEARS_FLUID_BLOCK.get(), "モブの涙液");
        add(FluidsRegistry.MOB_TEARS_FLUID_BUCKET.get(), "モブの涙液入りバケツ");
        add("fluid_type.woot_revived." + FluidsRegistry.MOB_TEARS_FLUID_TAG, "モブの涙液");

        add(FluidsRegistry.PURE_DYE_FLUID_BLOCK.get(), "純粋な染料液");
        add(FluidsRegistry.PURE_DYE_FLUID_BUCKET.get(), "純粋な染料液入りバケツ");
        add("fluid_type.woot_revived." + FluidsRegistry.PURE_DYE_FLUID_TAG, "純粋な染料液");

        add("info.woot_revived.mobshard.unprogrammed.desc", "モブを攻撃するか、モブシャードを投擲して記録しましょう");
        add("info.woot_revived.mobshard.unprogrammed", "プログラムされていない");
        add("info.woot_revived.mobshard.remaining.desc", "%sを倒すことでプログラムできます");
        add("info.woot_revived.mobshard.remaining.desc_no_entity", "モブを倒してモブシャードをプログラムしましょう");
        add("info.woot_revived.mobshard.remaining", "部分的にプログラム済み (必要討伐数:%d/%d)");
        add("info.woot_revived.mobshard.programmed", "完全にプログラム済み");
        add("info.woot_revived.shard.0", "右クリックで経験値を獲得");
        add("info.woot_revived.shard.1", "右クリックでスタック単位で使用できます");

        add("info.woot_revived.cell.amount", "バイタリティ燃料量");
        add("info.woot_revived.tier", "ティア");
        add("info.woot_revived.power", "電力");
        add("info.woot_revived.output_fluid", "搬出液体");
        add("info.woot_revived.output_amount", "搬出液体量");
        add("info.woot_revived.output", "搬出");
        add("info.woot_revived.input", "搬入");
        add("info.woot_revived.augment_input", "搬入2");
        add("info.woot_revived.input_fluid", "搬入液体");
        add("info.woot_revived.input_amount", "搬入液体量");
        add("info.woot_revived.dye.red", "赤");
        add("info.woot_revived.dye.yellow", "黄色");
        add("info.woot_revived.dye.blue", "青");
        add("info.woot_revived.dye.white", "白");
        add("info.woot_revived.base_item", "ベースアイテム");
        add("info.woot_revived.fluid", "液体");
        add("info.woot_revived.amount", "量");
        add("info.woot_revived.progress", "進捗");
        add("info.woot_revived.drained", "総消費量");
        add("info.woot_revived.eta", "処理時間");
        add("info.woot_revived.usage", "消費");
        add("info.woot_revived.cost", "コスト");
        add("info.woot_revived.rate", "所要時間");
        add("info.woot_revived.direction", "方向");
        add("info.woot_revived.action", "アクション");
        add("info.woot_revived.empty", "空");

        add("info.woot_revived.enchanted_liquifier.input", "エンチャントの本搬入");
        add("info.woot_revived.enchanted_liquifier.output", "エンチャント液搬出");
        add("info.woot_revived.dye_liquifier.red_input", "赤色の染料搬入");
        add("info.woot_revived.dye_liquifier.yellow_input", "黄色の染料搬入");
        add("info.woot_revived.dye_liquifier.blue_input", "青色の染料搬入");
        add("info.woot_revived.dye_liquifier.white_input", "白色の染料搬入");
        add("info.woot_revived.dye_liquifier.output", "純粋な染料液搬出");

        add("info.woot_revived.factory.invalid", "無効");
        add("info.woot_revived.factory.empty", "空");

        add("chat.woot_revived.anvil.cold", "金床はマグマブロックまたはマグマーターの上に設置されている必要があります");
        add("chat.woot_revived.anvil.nobase", "有効なベースアイテムを最初に置いてください");

        add("misc.woot_revived.tier_1", "銅");
        add("misc.woot_revived.tier_2", "鉄");
        add("misc.woot_revived.tier_3", "金");
        add("misc.woot_revived.tier_4", "ダイヤモンド");
        add("misc.woot_revived.tier_5", "ネザライト");

        add("gui.woot_revived.anvil.name", "スティジアンの金床");
        add("gui.woot_revived.enchanted_liquifier.name", "エンチャント液化機");
        add("gui.woot_revived.fluid_infuser.name", "液体注入機");
        add("gui.woot_revived.item_infuser.name", "アイテム注入機");
        add("gui.woot_revived.dye_liquifier.name", "染料液化機");
        add("gui.woot_revived.redstone.always_on", "レッドストーンモード: 無視");
        add("gui.woot_revived.redstone.with_no_signal", "レッドストーンモード: レッドストーン信号なしで実行");
        add("gui.woot_revived.redstone.with_signal", "レッドストーンモード: レッドストーン信号で実行");
        add("gui.woot_revived.redstone.once", "レッドストーンモード：パルスで実行");
        add("gui.woot_revived.heart.name", "ファクトリーハート");
        add("gui.woot_revived.heart.no_tier", "ファクトリーを建設する必要があります");
        add("gui.woot_revived.heart.insufficient_tier", "%sのティアが必要です");
        add("gui.woot_revived.heart.no_secondary", "2つ以上の疑似スポナーは設置されていません");
        add("gui.woot_revived.heart.vitality_cell_missing", "バイタリティセルがありません");
        add("gui.woot_revived.heart.vitality_cell", "バイタリティセル");

        add("jei.woot_revived.shard", "欠片アップグレードがインストールされたファクトリーが生産します。");
        add("jei.woot_revived.anvil.0", "金床を右クリックしてアイテムを追加し");
        add("jei.woot_revived.anvil.1", "スティジアンハンマーを使用することでクラフトできます。");
        add("jei.woot_revived.anvil.2", "スティジアンの金床はマグマブロック上に配置する必要があります。");
        add("jei.woot_revived.anvil.3", "疑似スポナーブロックをクラフトするには、完全にプログラムされたモブシャードが必要です。");
        add("jei.woot_revived.mob_shard.0", "モブシャードでモブを攻撃するか、投げつけることで、プログラミングを開始できます。");
        add("jei.woot_revived.mob_shard.1", "モブシャードを完全にプログラムするには、記録したモブを複数回倒す必要があります。");
        add("jei.woot_revived.mob_shard.2", "完全にプログラムすれば、スティジアンの金床を使用して疑似スポナーを作成できます。");
        add("jei.woot_revived.pure_dye_fluid", "染料液化機で青色%dmB、黄色%dmB、赤色%dmB、白色%dmBを使用して作成できます。");

        add("advancements.woot_revived.root.title", "Woot Revived");
        add("advancements.woot_revived.root.description", "このModが恋しかったんじゃない?");
        add("advancements.woot_revived.stygian_ingot.title", "インゴット1個おいくらですか!?");
        add("advancements.woot_revived.stygian_ingot.description", "すべての始まりであるスティジアンインゴットを作成する。");
        add("advancements.woot_revived.stygian_hammer.title", "必要なのはこれだけ!");
        add("advancements.woot_revived.stygian_hammer.description", "すべての始まりであるスティジアンのハンマーを作成する。");
        add("advancements.woot_revived.stygian_anvil.title", "モブの苦しみで工場を作ろう!");
        add("advancements.woot_revived.stygian_anvil.description", "すべての始まりであるスティジアンの金床を作成する。");
        add("advancements.woot_revived.shard_mold.title", "お気に入りの金型!");
        add("advancements.woot_revived.shard_mold.description", "スティジアンの金床で欠片の金型を作成する。");
        add("advancements.woot_revived.dye_mold.title", "嫌いな金型!");
        add("advancements.woot_revived.dye_mold.description", "スティジアンの金床で染料の型の金型を作成する。");
        add("advancements.woot_revived.plate_mold.title", "もう一つの板型?");
        add("advancements.woot_revived.plate_mold.description", "アイテム注入機で、ガラスと純粋な染料液を使ってプリズムを作成する。");
        add("advancements.woot_revived.mob_shard.title", "武装した敵なんていなかった、いいね?");
        add("advancements.woot_revived.mob_shard.description", "スティジアンの金床で欠片の金型を使ってモブシャードを作成する。");
        add("advancements.woot_revived.stygian_plate.title", "スティジアンの皿に盛られて");
        add("advancements.woot_revived.stygian_plate.description", "スティジアンの金床で、板金の金型を使ってスティジアンの板を作成する。");
        add("advancements.woot_revived.factory_base.title", "すべての問題の根源");
        add("advancements.woot_revived.factory_base.description", "ファクトリーベースを作成する。");
        add("advancements.woot_revived.fluid_infuser.title", "燃料によろしく伝えといて");
        add("advancements.woot_revived.fluid_infuser.description", "液体注入機を作成する。");
        add("advancements.woot_revived.mob_tears_bucket.title", "敵の涙は蜜の味!");
        add("advancements.woot_revived.mob_tears_bucket.description", "ネザライトのバイタリティセルを作成する。");
        add("advancements.woot_revived.vitality_fuel_bucket.title", "彼らの涙からバイタリティ燃料が???");
        add("advancements.woot_revived.vitality_fuel_bucket.description", "バイタリティ燃料液入りバケツを手に入れる。");
        add("advancements.woot_revived.enchanted_copper_plate.title", "光り輝く銅板?、うーん...");
        add("advancements.woot_revived.enchanted_copper_plate.description", "エンチャントされた銅板を作成する。");
        add("advancements.woot_revived.copper_cell.title", "このタンクは小さすぎる…");
        add("advancements.woot_revived.copper_cell.description", "銅のバイタリティセルを作成する。");
        add("advancements.woot_revived.enchanted_iron_plate.title", "輝く鉄板、いいね");
        add("advancements.woot_revived.enchanted_iron_plate.description", "エンチャントされた鉄板を作成する。");
        add("advancements.woot_revived.iron_cell.title", "タンクがいい感になってきた!");
        add("advancements.woot_revived.iron_cell.description", "鉄のバイタリティセルを作成する。");
        add("advancements.woot_revived.enchanted_gold_plate.title", "輝く金板、わあ！すごい!");
        add("advancements.woot_revived.enchanted_gold_plate.description", "エンチャントされた金板を作成する。");
        add("advancements.woot_revived.gold_cell.title", "このタンク、すごく明るい！");
        add("advancements.woot_revived.gold_cell.description", "金の土台を作成する。");
        add("advancements.woot_revived.enchanted_diamond_plate.title", "輝くダイヤモンドの板、なんて豪華なんだ!");
        add("advancements.woot_revived.enchanted_diamond_plate.description", "エンチャントされたダイヤモンドの板を作成する。");
        add("advancements.woot_revived.diamond_cell.title", "このタンクめっちゃ豪華だよ!");
        add("advancements.woot_revived.diamond_cell.description", "ダイヤモンドのバイタリティセルを作成する");
        add("advancements.woot_revived.enchanted_netherite_plate.title", "キラキラしたネザライトの板!!、最高!!!");
        add("advancements.woot_revived.enchanted_netherite_plate.description", "エンチャントされたネザライトの板を作成する。");
        add("advancements.woot_revived.netherite_cell.title", "このタンクは文字通り地獄から来たんだ!!!");
        add("advancements.woot_revived.netherite_cell.description", "ネザライトのバイタリティセルを作成する。");
        add("advancements.woot_revived.enchanted_liquifier.title", "その輝いてるの、欲しいんでしょ?");
        add("advancements.woot_revived.enchanted_liquifier.description", "エンチャント液化機を作成する。");
        add("advancements.woot_revived.enchanted_bucket.title", "輝くバケツを手に入れた!");
        add("advancements.woot_revived.enchanted_bucket.description", "エンチャント液入りバケツを手に入れる。");
        add("advancements.woot_revived.item_infuser.title", "アイテムに注入しよう!");
        add("advancements.woot_revived.item_infuser.description", "アイテム注入機を作成する。");
        add("advancements.woot_revived.dye_liquifier.title", "色を全部混ぜるのは良くないんじゃない?");
        add("advancements.woot_revived.dye_liquifier.description", "染料液化機を作成する。");
        add("advancements.woot_revived.pure_dye.title", "結果は虹だ!!!");
        add("advancements.woot_revived.pure_dye.description", "純粋な染料液入りバケツを手に入れる。");
        add("advancements.woot_revived.prism.title", "このガラスはすべての色を反射する!");
        add("advancements.woot_revived.prism.description", "アイテム注入機で、ガラスと純粋な染料液を使ってプリズムを作成する。");
        add("advancements.woot_revived.fake_spawner.title", "このスポナーは本当に存在するのか?");
        add("advancements.woot_revived.fake_spawner.description", "疑似スポナーを作成する。");
        add("advancements.woot_revived.black_dye_plate.title", "アップグレードの冒険、スタート!");
        add("advancements.woot_revived.black_dye_plate.description", "黒色の染料板を作成する。");
        add("advancements.woot_revived.upgrade_base.title", "アップグレードはどこにある!");
        add("advancements.woot_revived.upgrade_base.description", "アップグレードベースを作成する。");
        add("advancements.woot_revived.copper_shard.title", "さびた欠片!");
        add("advancements.woot_revived.copper_shard.description", "スティジアンの金床で銅の欠片を作成する。");
        add("advancements.woot_revived.copper_pylon.title", "最初のパイロン!");
        add("advancements.woot_revived.copper_pylon.description", "銅のパイロンを作成する");
        add("advancements.woot_revived.copper_plinth.title", "最初の土台!");
        add("advancements.woot_revived.copper_plinth.description", "銅の土台を作成する。");
        add("advancements.woot_revived.iron_shard_upgrade.title", "メタルシャードアップグレード!");
        add("advancements.woot_revived.iron_shard_upgrade.description", "鉄の欠片アップグレードを作成する。");
        add("advancements.woot_revived.iron_shard.title", "メタルシャード!");
        add("advancements.woot_revived.iron_shard.description", "ファクトリーから鉄の欠片を入手する。");
        add("advancements.woot_revived.iron_pylon.title", "2つ目のパイロン!");
        add("advancements.woot_revived.iron_pylon.description", "鉄のパイロンを作成する。");
        add("advancements.woot_revived.iron_plinth.title", "2個目の土台!");
        add("advancements.woot_revived.iron_plinth.description", "鉄の土台を作成する。");
        add("advancements.woot_revived.gold_shard_upgrade.title", "輝く欠片アップグレード!");
        add("advancements.woot_revived.gold_shard_upgrade.description", "金の欠片アップグレードを作成する。");
        add("advancements.woot_revived.gold_shard.title", "輝く欠片!");
        add("advancements.woot_revived.gold_shard.description", "金の欠片アップグレードを作成する。");
        add("advancements.woot_revived.gold_pylon.title", "3個目のパイロン!");
        add("advancements.woot_revived.gold_pylon.description", "金のパイロンを作成する。");
        add("advancements.woot_revived.gold_plinth.title", "3個目の土台!");
        add("advancements.woot_revived.gold_plinth.description", "金の土台を作成する。");
        add("advancements.woot_revived.diamond_shard_upgrade.title", "豪華な欠片アップグレード!");
        add("advancements.woot_revived.diamond_shard_upgrade.description", "ダイヤモンドの欠片アップグレードを作成する。");
        add("advancements.woot_revived.diamond_shard.title", "豪華な欠片!");
        add("advancements.woot_revived.diamond_shard.description", "ファクトリーからダイヤモンドの欠片を入手する。");
        add("advancements.woot_revived.diamond_pylon.title", "4個目のパイロン!");
        add("advancements.woot_revived.diamond_pylon.description", "ダイヤモンドのパイロンを作成する。");
        add("advancements.woot_revived.diamond_plinth.title", "4個目の土台!");
        add("advancements.woot_revived.diamond_plinth.description", "ダイヤモンドの土台を作成する");
        add("advancements.woot_revived.netherite_shard_upgrade.title", "地獄の欠片アップグレード!");
        add("advancements.woot_revived.netherite_shard_upgrade.description", "ネザライトの欠片アップグレードを作成する。");
        add("advancements.woot_revived.netherite_shard.title", "地獄の欠片!");
        add("advancements.woot_revived.netherite_shard.description", "ファクトリーからネザライトの欠片を入手する。");
        add("advancements.woot_revived.netherite_pylon.title", "5個目にして最後のパイロン!");
        add("advancements.woot_revived.netherite_pylon.description", "ネザライトのパイロンを作成する。");
        add("advancements.woot_revived.netherite_plinth.title", "5個目にして最後の土台!");
        add("advancements.woot_revived.netherite_plinth.description", "ネザライトの土台を作成する。");
    }

    protected void addBlock(Block block, String translation){
        add(block, translation);
        add(block.asItem(), translation);
    }
}
